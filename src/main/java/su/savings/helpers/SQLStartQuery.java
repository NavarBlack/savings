package su.savings.helpers;

public abstract class SQLStartQuery {
    public static String createAllPlansTable = """
            create table if not exists ALL_PLANS
                                        (
                                        	ID BIGINT auto_increment,
                                        	PLAN_NAME CHAR(20) not null,
                                        	START_PLAN DATE not null,
                                        	END_PLAN DATE not null,
                                        	START_SUM BIGINT not null,
                                        	PLAN_DAYS BIGINT not null
                                        );
                            
                                        create unique index if not exists ALL_PLANS_ID_uindex
                                        	on ALL_PLANS (ID);
                            
                                        create unique index if not exists ALL_PLANS_PLAN_NAME_uindex
                                        	on ALL_PLANS (PLAN_NAME);
                            
                                        alter table  ALL_PLANS
                                        	add constraint if not exists ALL_PLANS_pk
                                        		primary key (ID);
            """;
    public static String createKeyPointsTable = """
            create table if not exists KEY_POINTS
             (
                 ID         BIGINT auto_increment,
                 DATE_POINT DATE   not null,
                 PLAN_ID    BIGINT not null,
                 constraint KEY_POINTS_ALL_PLANS_ID_fk
                     foreign key (PLAN_ID) references ALL_PLANS on delete cascade
             );
             
             create unique index if not exists KEY_POINTS_ID_uindex
                 on KEY_POINTS (ID);
             
             alter table  KEY_POINTS
                 add constraint if not exists KEY_POINTS_pk
                     primary key (ID);
            """;
    public static String createAllPeriods = """
            create table if not exists ALL_PERIODS
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
            	constraint ALL_PERIODS_ALL_PLANS_ID_fk
            		foreign key (PLAN_ID) references ALL_PLANS on delete cascade
            );
                        
            create unique index if not exists ALL_PERIODS_ID_uindex
            	on ALL_PERIODS (ID);
                        
            alter table ALL_PERIODS
            	add constraint if not exists ALL_PERIODS_pk
            		primary key (ID);
            """;
    public static String createOperations = """
            create table if not exists OPERATIONS
            (
            	ID BIGINT auto_increment,
            	OPERATION_NAME CHAR(20) not null,
            	SUM BIGINT not null,
            	PERIOD_ID int not null,
            	EXP_TYPE BOOL default false not null,
            	NPO_DATE DATE,
            	NPO_TYPE BOOL default false not null,
            	constraint OPERATIONS_ALL_PERIODS_ID_fk
            		foreign key (PERIOD_ID) references ALL_PERIODS on delete cascade
            );
                        
            create unique index if not exists OPERATIONS_ID_uindex
            	on OPERATIONS (ID);
                        
            alter table OPERATIONS
            	add constraint if not exists OPERATIONS_pk
            		primary key (ID);
            """;
}
