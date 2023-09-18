use Sab
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

INSERT INTO Voznja 
VALUES (47,1137,463)
go

use Sab 
go
delete from Voznja
go