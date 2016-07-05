package ru.nabsky.dao;


import ru.nabsky.models.Token;

public interface TokenDAO extends CommonDAO<Token> {

    Token findTokenByTeamId(String teamId, Token.TokenMode tokenMode);

    Token getToken(String teamName, String password);

}
