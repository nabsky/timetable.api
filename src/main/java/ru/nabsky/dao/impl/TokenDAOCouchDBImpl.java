package ru.nabsky.dao.impl;

import com.google.inject.Inject;
import lombok.extern.java.Log;
import ru.nabsky.dao.TeamDAO;
import ru.nabsky.dao.TokenDAO;
import ru.nabsky.helper.SecurityHelper;
import ru.nabsky.models.Team;
import ru.nabsky.models.Token;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Log
public class TokenDAOCouchDBImpl extends CommonDAOCouchDBImpl<Token> implements TokenDAO {

    @Inject
    public TokenDAOCouchDBImpl() {
    }

    @Override
    public Token findTokenByTeamId(String teamId, Token.TokenMode tokenMode) {
        List<Token> tokens = getConnection().view("tokens/by_team")
                .key(teamId, tokenMode)
                .includeDocs(true)
                .query(Token.class);
        closeConnection();
        if (tokens.size() == 0) {
            return null;
        } else {
            return tokens.get(0);
        }
    }

    public Token getToken(String teamName, String password) {
        String secret = SecurityHelper.getSecret(password);
        TeamDAO teamDAO = new TeamDAOCouchDBImpl();
        Team team = teamDAO.findByName(teamName);
        if (team == null) {
            return null;
        }

        String teamId = team.get_id();
        Token.TokenMode tokenMode;

        if (secret.equals(team.getMatePassword())) {
            tokenMode = Token.TokenMode.MATE;
        } else if (secret.equals(team.getLeadPassword())) {
            tokenMode = Token.TokenMode.LEAD;
        } else {
            return null;
        }
        Token token = findTokenByTeamId(teamId, tokenMode);

        if (token == null || token.isExpired()) {
            if (token != null) {
                delete(token);
            }
            token = new Token();
            token.setTeamId(teamId);
            token.setMode(tokenMode);
            String tokenId = insert(token);
            token = findById(tokenId);
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 15);
        token.setExpirationDate(c.getTime());
        update(token);

        return token;
    }

}
