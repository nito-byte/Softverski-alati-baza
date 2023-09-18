/*ovde sada zelim da kreiram StoredProceduru 
  grantRequest(String userName)*/

  USE SAB
  GO

  CREATE PROC spGrantRequest
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

	RETURN 1

  END
  GO

  DECLARE @result int
  EXEC @result=spGrantRequest 'tiki'

  SELECT @result


 