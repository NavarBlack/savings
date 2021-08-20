package su.savings.helpers;

public abstract class SQLStartQuery {
    public static String createAllPlansTable = """
            create table if not exists PLANS
                                        (
                                        	ID BIGINT auto_increment,
                                        	PLAN_NAME CHAR(20) not null,
                                        	START_PLAN DATE not null,
                                        	END_PLAN DATE not null,
                                        	START_SUM BIGINT not null,
                                        	PLAN_DAYS BIGINT not null
                                        );
                            
                                        create unique index if not exists ALL_PLANS_ID_uindex
                                        	on PLANS (ID);
                            
                                        create unique index if not exists ALL_PLANS_PLAN_NAME_uindex
                                        	on PLANS (PLAN_NAME);
                            
                                        alter table  PLANS
                                        	add constraint if not exists ALL_PLANS_pk
                                        		primary key (ID);
            """;
    public static String createKeyPointsTable = """
            create table if not exists POINTS
             (
                 ID         BIGINT auto_increment,
                 DATE_POINT DATE   not null,
                 PLAN_ID    BIGINT not null,
                 constraint if not exists KEY_POINTS_ALL_PLANS_ID_fk
                     foreign key (PLAN_ID) references PLANS on delete cascade
             );
             
             create unique index if not exists KEY_POINTS_ID_uindex
                 on POINTS (ID);
             
             alter table  POINTS
                 add constraint if not exists KEY_POINTS_pk
                     primary key (ID);
            """;
    public static String createAllPeriods = """
            create table if not exists PERIODS
            (
            	ID BIGINT auto_increment,
            	START_PERIOD DATE not null,
            	END_PERIOD DATE not null,
            	START_SUM BIGINT,
            	END_SUM BIGINT,
            	EXP_ON_DEY BIGINT not null,
            	PERIOD_DAYS BIGINT not null,
            	PAST_DAYS BIGINT not null,
            	PLAN_ID BIGINT not null,
            	constraint if not exists ALL_PERIODS_ALL_PLANS_ID_fk
            		foreign key (PLAN_ID) references PLANS on delete cascade
            );
                        
            create unique index if not exists ALL_PERIODS_ID_uindex
            	on PERIODS (ID);
                        
            alter table PERIODS
            	add constraint if not exists ALL_PERIODS_pk
            		primary key (ID);
            """;
    public static String createDays = """
            create table if not exists DAYS
            (
            	ID BIGINT auto_increment,
            	DAY_DATE DATE not null,
            	REMAINS_START BIGINT default 0 not null,
            	REMAINS_FACT BIGINT default 0 not null,
            	ACCUMULATION BIGINT default 0 not null,
            	EXP_DAY BIGINT not null,
            	REMAINS_END BIGINT not null,
            	REMAINS_END_DEFICIT BIGINT default 0 not null,
            	REMAINS_END_PROFIT BIGINT default 0 not null,
            	PERIOD_ID bigint not null,
            	constraint if not exists DAYS_ALL_PERIODS_ID_fk
            		foreign key (PERIOD_ID) references PERIODS on delete cascade
            );
            
            create unique index if not exists DAYS_ID_uindex
            	on DAYS (ID);
            
            alter table DAYS
            	add constraint if not exists DAYS_pk
            		primary key (ID);
            """;
    public static String createOperations = """
            create table if not exists OPERATIONS
            (
            	ID BIGINT auto_increment,
            	OPERATION_NAME CHAR(20) not null,
            	SUM BIGINT not null,
            	EXP_TYPE BOOL default false not null,
            	NPO_DATE DATE,
            	NPO_TYPE BOOL default false not null,
            	PERIOD_ID BIGINT,
            	DAY_ID BIGINT,
            	constraint OPERATIONS_ALL_PERIODS_ID_fk
            		foreign key (PERIOD_ID) references PERIODS on delete cascade,
            	constraint OPERATIONS_DAYS_ID_fk
            		foreign key (DAY_ID) references PERIODS on delete cascade
            );
                        
            create unique index if not exists OPERATIONS_ID_uindex
            	on OPERATIONS (ID);
                        
            alter table OPERATIONS
            	add constraint if not exists OPERATIONS_pk
            		primary key (ID);
            """;


}
