package ru.nabsky.module;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import ru.nabsky.dao.*;
import ru.nabsky.dao.factories.DayDAOFactory;
import ru.nabsky.dao.factories.MateDAOFactory;
import ru.nabsky.dao.factories.TimeDAOFactory;
import ru.nabsky.dao.factories.UnitDAOFactory;
import ru.nabsky.dao.impl.*;
import ru.nabsky.services.TeamService;
import ru.nabsky.services.impl.TeamServiceImpl;

public class BindingModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(MateDAO.class, MateDAOCouchDBImpl.class)
                .build(MateDAOFactory.class));

        install(new FactoryModuleBuilder()
                .implement(UnitDAO.class, UnitDAOCouchDBImpl.class)
                .build(UnitDAOFactory.class));

        install(new FactoryModuleBuilder()
                .implement(TimeDAO.class, TimeDAOCouchDBImpl.class)
                .build(TimeDAOFactory.class));

        install(new FactoryModuleBuilder()
                .implement(DayDAO.class, DayDAOCouchDBImpl.class)
                .build(DayDAOFactory.class));

        bind(TeamDAO.class).to(TeamDAOCouchDBImpl.class);

        bind(TokenDAO.class).to(TokenDAOCouchDBImpl.class);

        bind(TeamService.class).to(TeamServiceImpl.class);
    }
}