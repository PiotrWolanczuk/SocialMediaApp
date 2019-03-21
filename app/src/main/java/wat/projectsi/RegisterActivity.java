package wat.projectsi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/*
Oznaczenie	PU1
Nazwa	Zarejestrowanie
Opis	Założenie nowego konta użytkownika w portalu.
Aktorzy	Gość
Warunki wstępne	Kliknięcie przycisku „Zarejestruj”
Rezultat	Utworzenie nowego konta

Scenariusz
1.	Wybranie opcji rejestracji
2.	Wypełnienie formularza (Imię, Nazwisko, e-mail, hasło, potwierdzenie hasła)
3.	Zaakceptowanie regulaminu strony
4.	Potwierdzenie rejestracji
5.	Udane zakończenie procesu rejestracji

Scenariusz alternatywny	2.1 Błędne wypełnienie formularza (niezgodne hasła, niepoprawny email)
2.2. Wyświetlenie okienka z informacją “Nieprawidłowe dane”
3.1 Niezaakceptowanie regulaminu strony
3.2 Wyświetlenie okienka z informacją “Zaakceptuj regulamin”

 */
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
