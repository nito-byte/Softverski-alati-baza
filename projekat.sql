use dd
go


/********************************************/
CREATE TABLE Grad (
    IDGra int IDENTITY(1,1) PRIMARY KEY,
    Naziv char(100) NOT NULL,
    PostanskiBroj char(100) NOT NULL
);
go

ALTER TABLE Grad
ADD CONSTRAINT UC_Grad UNIQUE (Naziv, PostanskiBroj);
go

ALTER TABLE Grad
ADD UNIQUE (Naziv);
go

ALTER TABLE Grad
ADD UNIQUE (PostanskiBroj);
go

/******************************************************/


CREATE TABLE [Opstina]
( 
	[IDOpstina]          int IDENTITY(1,1) PRIMARY KEY ,
	[Naziv]              char(100) not NULL ,
	[IDgra]              int not NULL ,
	[X]                  int not NULL ,
	[Y]                  int not NULL 
)
go


ALTER TABLE [Opstina]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IDgra]) REFERENCES [Grad]([IDgra])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

/*****************************************************/

CREATE TABLE [Korisnik]
( 
	[IDKorisnik]         int IDENTITY(1,1) PRIMARY KEY,
	[Ime]                char(100)   not NULL ,
	[Prezime]            char(100)  not NULL ,
	[Username]           char(100)  unique not NULL ,
	[Password]           char(100)  not NULL ,
	[BrojPoslatihPaketa] int   default 0
)
go

ALTER TABLE [Korisnik]
    ADD CONSTRAINT [OgranicenjeIme] CHECK (ASCII(LEFT(Ime, 1)) BETWEEN ASCII('A') and ASCII('Z'));
go


ALTER TABLE [Korisnik]
    ADD CONSTRAINT [OgranicenjePrezime] CHECK (ASCII(LEFT(Prezime, 1)) BETWEEN ASCII('A') and ASCII('Z'));
go

ALTER TABLE [Korisnik]
    ADD CONSTRAINT [OgranicenjePassword] CHECK (LEN(Password) >= 8)
										 
go

ALTER TABLE [Korisnik]
    ADD CONSTRAINT [OgranicenjePassword1] CHECK (Password like '%[0-9]%' and password like '%[A-Z]%')
										 
go


/**************************************************************/

  CREATE TABLE [Admin]
( 
	[IDKorisnik]         int  NOT NULL 
)
go

ALTER TABLE [Admin]
	ADD CONSTRAINT [XPKE_2] PRIMARY KEY  CLUSTERED ([IDKorisnik] ASC)
go


ALTER TABLE [Admin]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([IDKorisnik]) REFERENCES [Korisnik]([IDKorisnik])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

/************************************************/

CREATE TABLE [Vozilo]
( 
	[IDVozilo]         int IDENTITY(1,1) PRIMARY KEY,
	[RegistracioniBroj]  char(100)   not NULL ,
	[TipGoriva]            int  not NULL ,
	[Potrosnja]           decimal(10,3) not null
)
go


/*************************************************/




CREATE TABLE [Kurir]
( 
	
	[IDKorisnik]         int not null,
	[IDVozilo]           int not NULL ,
	[BrojIsporucenihPaketa] int  NULL ,
	[OstvarenProfit]     decimal(10,3)  null ,
	[Status]             int  NULL 
)
go

/*
ALTER TABLE [Kurir]
	ADD CONSTRAINT [XP_2] PRIMARY KEY  CLUSTERED ([IDKorisnik] ASC)
go
*/


ALTER TABLE Kurir
ADD CONSTRAINT IDKOR_UNIQ UNIQUE (IDKorisnik,IDVozilo);
go

ALTER TABLE Kurir
ADD UNIQUE (IDKorisnik);
go

ALTER TABLE Kurir
ADD UNIQUE (IDVozilo);
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([IDKorisnik] ASC,[IDVozilo] ASC)
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([IDVozilo]) REFERENCES [Vozilo]([IDVozilo])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([IDKorisnik]) REFERENCES [Korisnik]([IDKorisnik])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

/*******************************************************/


CREATE TABLE [KurirZahtev]
( 
	[IDKurirZahtev]      int IDENTITY(1,1) PRIMARY KEY,
	[IDKorisnik]         int  not NULL ,
	[IDVozilo]           int not  NULL 
	
)
go



ALTER TABLE [KurirZahtev]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([IDKorisnik]) REFERENCES [Korisnik]([IDKorisnik])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [KurirZahtev]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([IDVozilo]) REFERENCES [Vozilo]([IDVozilo])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

/**********************************************/

/*
ALTER TABLE [KurirZahtev]
	ADD CONSTRAINT [XPKKurirZahtev] PRIMARY KEY  CLUSTERED ([IDKorisnik] ASC,[IDVozilo] ASC)
go
*/




CREATE TABLE [Paket]
( 
	[IDPaket]            int IDENTITY(1,1) PRIMARY KEY ,
	[TipPaketa]          int  not NULL ,
	[TezinaPaketa]       decimal(10,3)  not NULL ,
	[Status]             int  not NULL ,
	[Cena]               decimal(10,3)  NULL ,
	[VremePrihvatanjaZahteva] datetime  NULL ,
	[OpstinaPreuzima]    int  not NULL ,
	[OpstinaDostavlja]   int  not NULL ,
	[IDKorisnik]         int  not NULL 
)
go


  ALTER TABLE Paket ALTER COLUMN VremePrihvatanjaZahteva datetime2;
  go

  
ALTER TABLE [Paket]
	ADD CONSTRAINT [R_19] FOREIGN KEY ([OpstinaPreuzima]) REFERENCES [Opstina]([IDOpstina])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_20] FOREIGN KEY ([OpstinaDostavlja]) REFERENCES [Opstina]([IDOpstina])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_21] FOREIGN KEY ([IDKorisnik]) REFERENCES [Korisnik]([IDKorisnik])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go




/**********************************************************/



CREATE TABLE [Ponuda]
( 
	[IDPonuda]           int IDENTITY(1,1) PRIMARY KEY ,
	[ProcenatCeneIsporuke] decimal(10,3)  not NULL ,
	[IDPaket]            int  not NULL ,
	[IDKorisnik]         int  not NULL ,
	[IDVozilo]           int  not NULL 
)
go


ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_23] FOREIGN KEY ([IDKorisnik],[IDVozilo]) REFERENCES [Kurir]([IDKorisnik],[IDVozilo])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_22] FOREIGN KEY ([IDPaket]) REFERENCES [Paket]([IDPaket])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

/******************************************************/




CREATE TABLE [Voznja]
( 
	[IDVoznja]           int IDENTITY(1,1) PRIMARY KEY,
	[IDPaket]            int  not NULL ,
	[IDKorisnik]         int  not NULL ,
	[IDVozilo]           int  not NULL 
	
)
go

/*
ALTER TABLE [Voznja]
	ADD CONSTRAINT [R_24Voznja] FOREIGN KEY ([IDPaket]) REFERENCES [Paket]([IDPaket])
		
go

ALTER TABLE [Voznja]
	ADD CONSTRAINT [R_25Voznja] FOREIGN KEY ([IDKorisnik],[IDVozilo]) REFERENCES [Kurir]([IDKorisnik],[IDVozilo])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go
*/
 

  CREATE TABLE [Pomocna]
( 
	[IDPomocna]    int IDENTITY(1,1) PRIMARY KEY,
	[Cena]         DECIMAL(10,3) NOT NULL ,
	[Loss]         DECIMAL(10,3) NOT NULL ,
	[X1]           int  not NULL ,
	[Y1]           int  not NULL ,
	[X2]           int  not NULL ,
	[Y2]           int  not NULL 
	
)
go

 CREATE PROC [dbo].[spGrantRequest]
  (
	@userName AS CHAR(100)

  )
  AS
  BEGIN
	DECLARE @IDKor AS INT
	DECLARE @IDVoz AS INT


	/*prvo treba da iz tabele Korisnik dohvatim IDKorisnika za to username*/
	SELECT @IDKor = IDKorisnik
	FROM Korisnik
	WHERE Username = @userName

	/*u tabeli KurirZahtev treba dohvatim KurirZahtev za dati IDKorisnik
      sa odgovarajucim IDVozilo. Ako ih ima vise onda uzeti prvi u tabeli*/
	SELECT top 1 @IDVoz = IDVozilo
	FROM KurirZahtev
	WHERE IDKorisnik = @IDKor

	/*ovde treba i da obrisem red iz tabele KurirZahtev*/
	DELETE KurirZahtev
	WHERE IDKorisnik = @IDKor

	/*treba da ubacim red u tabelu Kurir*/
	INSERT INTO Kurir
	VALUES (@IDKor, @IDVoz,0,0,0)
	



  END

GO


use  dd
go
CREATE TRIGGER okidac
ON Voznja
AFTER INSERT
AS
BEGIN
DECLARE @IdPaket int, 
	    @IdKorisnik int,
		@IdVozilo int

	SET @IdPaket = (SELECT IDPaket FROM inserted)
	SET @IdKorisnik= (SELECT IDKorisnik FROM inserted)
	SET @IdVozilo= (SELECT IDVozilo FROM inserted)

	/* treba da izbrisem sve ponude iz tabele ponuda koje su sa tim ID-em*/
	DELETE FROM Ponuda
	WHERE IDPaket = @IdPaket

	/*treba da postavim status paketa na zahtev prihvacen tj 1*/
	UPDATE Paket
	SET Status = 1
	WHERE IDPaket = @IdPaket

	/*treba da postavim vreme prihvatanja zahteva*/
	UPDATE Paket
	SET VremePrihvatanjaZahteva = SYSDATETIME()
	WHERE IDPaket = @IdPaket



	RETURN
END
go

use sab
  go
  BACKUP DATABASE Sab TO DISK='C:\sab\Sab.bak'
  go