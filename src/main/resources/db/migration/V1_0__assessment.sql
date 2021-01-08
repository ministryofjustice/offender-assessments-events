create table OFFENDER
(
	OFFENDER_PK NUMBER not null
		constraint OFFENDER_PK
			primary key,
	PNC VARCHAR2(20)
);

create table OASYS_ASSESSMENT_GROUP
(
    OASYS_ASSESSMENT_GROUP_PK NUMBER not null
        constraint OASYS_ASSESSMENT_GROUP_PK
            primary key,
    OFFENDER_PK NUMBER not null
        constraint OFF_OAG
            references OFFENDER
            on delete cascade
);

create table OASYS_SET
(
    OASYS_SET_PK              NUMBER not null
        constraint OASYS_SET_PK
            primary key,
    ASSESSMENT_STATUS_ELM     VARCHAR2(50) not null,
    ASSESSMENT_TYPE_ELM       VARCHAR2(50),
    OASYS_ASSESSMENT_GROUP_PK NUMBER not null
        constraint OAG_OAS
            references OASYS_ASSESSMENT_GROUP
            on delete cascade,
    DATE_COMPLETED DATE
);

