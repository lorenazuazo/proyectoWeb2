insert into authority(id,authority)
values(1,"ROLE_ADMIN"),
(2,"ROLE_USER");
 

insert into usuario(apellido,password,correo,dni,nombre,telefono,username) 
values("zuazo","$2a$04$PcsqHPhqPiPaZd9vVW.Ahu6exv7fiiiiJm2/g/UnwuhaufIRKEJom","loren@gmail.com","2548256","lorena","24585652","lore"),
("gaitan","$2a$04$PcsqHPhqPiPaZd9vVW.Ahu6exv7fiiiiJm2/g/UnwuhaufIRKEJom","fabio@gmail.com","25558256","fabio","245869852","fabi"),
("sanchez","$2a$04$PcsqHPhqPiPaZd9vVW.Ahu6exv7fiiiiJm2/g/UnwuhaufIRKEJom","sanch@gmail.com","25698256","maria","24585652","maria");


insert into users_authorities(usuario_id,authority_id)
values(1,1),
(2,2),
(3,2);

insert into tipohabitacion(clase,descripcion,dimension,camas,estado)
values("Individual","habitacion para 1 o 2 personas","10m cuad","1 doble o 2 simples","VIGENTE"),
("Junior suite","habitacion hasta 3 personas","19m cuad","1 queen o 1 simple","VIGENTE"),
("Doble","habitacion para 1 o 2 personas","10m cuad","1 doble o 2 simples","VIGENTE"),
("Superior","habitacion hasta 3 personas","15m cuad","1 doble o 1 simples","VIGENTE"),
("Estudio","habitacion hasta 3 personas c/estudio","30m cuad","1 doble o 1 queen","VIGENTE"),
("Quad","habitacion hasta 4 personas","30m cuad","2 doble o 2 queen","VIGENTE");

insert into caracteristicashabitacion(detalle,estado)
values("aire acondicionado","VIGENTE"),
("minibar","VIGENTE"),
("yacuzzi","VIGENTE"),
("secador de pelo","VIGENTE"),
("caja de seguridad","VIGENTE");

insert into habitacion(numerohabitacion,id_tipohabitacion,tarifa,descripcion,cantidadhuesped,estado)
values("101",1,2500,"habitacion con balcon",2,"VIGENTE"),
("102",2,2600,"habitacion con vista a la calle",3,"VIGENTE"),
("103",3,3500,"habitacion sin vista",2,"VIGENTE"),
("104",4,3500,"habitacion con estudio",3,"VIGENTE"),
("105",5,3800,"habitacion",3,"VIGENTE"),
("106",6,3800,"habitacion con alfombra",4,"VIGENTE"),
("107",2,3900,"habitacion junior",3,"VIGENTE"),
("108",3,4500,"habitacion dos balcones",2,"VIGENTE");

insert into habitacion_caracthabitacion(habitacion_id,caracthabitacion_id)
values(1,1),
(1,3),
(2,1),
(2,2),
(2,4),
(3,1),
(3,2),
(3,4),
(4,1),
(4,2),
(4,3),
(5,1),
(5,2),
(5,3),
(6,1),
(6,2),
(6,3),
(6,4),
(6,5),
(7,4),
(8,4);