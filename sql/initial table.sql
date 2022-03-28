create table if not exists Barber
(
    id integer not null
        primary key AUTOINCREMENT ,
    code varchar(255) null,
    name varchar(255) null
);

create table if not exists CategoryService
(
    id integer not null
        primary key AUTOINCREMENT ,
    code varchar(255) null,
    name varchar(255) null,
    price varchar(255) null
);

create table if not exists Book
(
    id integer not null
        primary key autoincrement ,
    code varchar(255) null,
    dateTime datetime(6) null,
    mobileNumber int not null,
    name varchar(255) null,
    status varchar(255) null,
    barberName_id bigint null,
    categoryService_id bigint null,
    constraint FK21uqlu6tjgfkq6dhmjc6bya4q
        foreign key (categoryService_id) references CategoryService (id),
    constraint FKqrtmn5nl2kx9li59raeel8o1s
        foreign key (barberName_id) references Barber (id)
);