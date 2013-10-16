create table if not exists game (
  id int unsigned not null auto_increment,
  state enum('ONGOING', 'X_VICTORY', 'O_VICTORY', 'CATS_GAME') not null,
  board varchar(255) not null,

  primary key (id)
);

create table if not exists move (
  id int unsigned not null auto_increment,
  game_id int unsigned not null,
  player enum('X', 'O') not null,
  x tinyint unsigned not null,
  y tinyint unsigned not null,

  primary key (id),
  constraint game_fk foreign key (game_id) references game (id)
);
