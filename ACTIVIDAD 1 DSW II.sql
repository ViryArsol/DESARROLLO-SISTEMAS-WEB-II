/***** Viridiana Armenta Solano  ****/

/****** Base de datos para Sistemas Web  ******/

/*Se crea base de datos*/
CREATE DATABASE TiendaSara;

/*Se usa la base para crear las tablas dentro de este*/
USE TiendaSara;

/*Se crean las tablas independientes (no cuentan con llaves foraneas)*/
CREATE TABLE Categoria (
IDcategoria INT IDENTITY(1,1) PRIMARY KEY,
DescripcionCategoria VARCHAR(100) NOT NULL
);

CREATE TABLE Marcas (
IDmarca INT IDENTITY (1,1) PRIMARY KEY,
DescripcionMarca VARCHAR (100) NOT NULL
);

CREATE TABLE Carrito (
IDcarrito INT IDENTITY(1,1) PRIMARY KEY,
FolioVenta VARCHAR(20) NOT NULL,
TotalCompra DECIMAL(10,2) NOT NULL,
Estatus VARCHAR(30) NOT NULL DEFAULT 'Pendiente',
Fecha DATETIME DEFAULT GETDATE()
);

/*Se crean las tablas con llave foranea*/
CREATE TABLE Producto (
IDproducto INT IDENTITY(1,1) PRIMARY KEY,
DescripcionProducto VARCHAR (100) NOT NULL,
Cantidad INT,
IDcategoria INT NOT NULL,
IDmarca INT NOT NULL,
FOREIGN KEY (IDcategoria) REFERENCES Categoria(IDcategoria),
FOREIGN KEY (IDmarca) REFERENCES Marcas(IDmarca),
);

CREATE TABLE CarritoDetalle (
IDcarritoDetalle INT IDENTITY(1,1) PRIMARY KEY,
IDcarrito INT NOT NULL,
IDproducto INT NOT NULL,
Cantidad INT,
Subtotal DECIMAL (10,2) NOT NULL,
FOREIGN KEY (IDcarrito) REFERENCES Carrito(IDcarrito),
FOREIGN KEY (IDproducto) REFERENCES Producto(IDproducto),
);

/**Selección de tablas creadas**/
SELECT *
FROM Carrito;

SELECT *
FROM CarritoDetalle;

SELECT *
FROM Categoria;

SELECT *
FROM Marcas;

SELECT *
FROM Producto;

/**Ingreso de valores, se inicia con las que no tienen llaves foraneas**/
INSERT INTO Categoria(DescripcionCategoria) 
VALUES
('Electrónica'),
('Línea Blanca'),
('Computación'),
('Ropa y Moda'),
('Hogar y Jardín'),
('Deportes'),
('Juguetes'),
('Belleza y Cuidado Personal'),
('Alimentos y Bebidas'),
('Libros y Papelería');

INSERT INTO Marcas(DescripcionMarca)
VALUES
('Samsung'),
('Sony'),
('LG'),
('Nike'),
('Adidas'),
('Whirlpool'),
('HP'),
('Lenovo'),
('Apple'),
('Philips');

INSERT INTO Carrito(FolioVenta,TotalCompra,Estatus,Fecha)
VALUES
('FOL-2026-001','1250.50','Pagado','2026.10.01 10:30:00'),
('FOL-2026-002','430.00','Pendiente','2026-10-02 14:15:00'),
('FOL-2026-003','899.99','En Proceso','2026-10-03 09:20:00'),
('FOL-2026-004','150.00','Cancelado','2026-10-04 16:45:00'),
('FOL-2026-005','3200.00','Pagado','2026-10-05 11:10:00'),
('FOL-2026-006','750.25','Pendiente','2026-10-06 18:30:00'),
('FOL-2026-007','540.00','En Proceso','2026-10-07 12:00:00'),
('FOL-2026-008','2100.50','Pagado','2026-10-08 8:50:00'),
('FOL-2026-009','95.00','Pendiente','2026-10-09 19:25:00'),
('FOL-2026-010','1450.00','Pagado','2026-10-10 13:05:00');

INSERT INTO Producto(DescripcionProducto,Cantidad, IDcategoria, IDmarca)
VALUES
('Smart TV 55 Pulgadas','15','1','1'),
('Audífonos Inalámbrico','40','1','2'),
('Refrigerador Automático','5','2','6'),
('Laptop Core i5','12','3','7'),
('Tenis Deportivos Running','30','4','4'),
('Playera Deportiva','50','4','5'),
('Licuadora de 3 Velocidades','20','2','10'),
('Mouse Óptico USB','60','3','8'),
('Smartphone de Gama Media','25','1','9'),
('Mochila para Laptop','35','10','8');

INSERT INTO CarritoDetalle(IDcarrito,IDproducto, Cantidad, Subtotal)
VALUES
('1','1','1','1200.00'),
('1','8','1','50.50'),
('2','5','2','430.00'),
('3','3','1','899.99'),
('4','6','3','150.00'),
('5','4','1','3200.00'),
('6','2','2','750.25'),
('7','7','1','540.00'),
('8','9','1','2100.50'),
('9','10','1','95.00');

/**Consultas con INNER JOIN**/
SELECT 
	P.IDproducto,
	P.DescripcionProducto,
	C.DescripcionCategoria,
	M.DescripcionMarca
FROM Producto AS P
INNER JOIN Categoria AS C ON P.IDcategoria = C.IDcategoria
INNER JOIN Marcas AS M ON P.IDmarca = M.IDmarca;

SELECT
	D.IDcarritoDetalle,
	D.Subtotal,
	C.Estatus,
	P.DescripcionProducto
FROM CarritoDetalle AS D
INNER JOIN Carrito AS C ON D.IDcarrito = C.IDcarrito
INNER JOIN Producto AS P ON D.IDproducto = P.IDproducto;

SELECT
	D.IDcarritoDetalle,
	C.IDcarrito,
	C.FolioVenta,
	P.DescripcionProducto,
	G.DescripcionCategoria,
	M.DescripcionMarca
FROM CarritoDetalle AS D
INNER JOIN Carrito AS C ON D.IDcarrito = C.IDcarrito
INNER JOIN Producto AS P ON D.IDproducto = P.IDproducto
INNER JOIN Categoria AS G ON P.IDcategoria = G.IDcategoria
INNER JOIN Marcas AS M ON P.IDmarca = M.IDmarca;



