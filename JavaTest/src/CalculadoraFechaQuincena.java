import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CalculadoraFechaQuincena {

    List<LocalDate> diasFestivos = new ArrayList<>();

    public static void main(String[] args) {
        // Crear un objeto Scanner para recibir la entrada del usuario
        Scanner scanner = new Scanner(System.in);

        List<LocalDate> diasFestivos = new ArrayList<>();
        diasFestivos.add(LocalDate.of(2023, 7, 31));
        diasFestivos.add(LocalDate.of(2024, 1, 1));   // Año Nuevo

        CalculadoraFechaQuincena cfq = new CalculadoraFechaQuincena();
        cfq.diasFestivos = diasFestivos;

        try {
            // Solicitar al usuario ingresar la fecha en formato "yyyy-MM-dd"
            System.out.print("Ingrese la fecha en formato 'yyyy-MM-dd': ");
            String inputFecha = scanner.nextLine();

            // Convertir la cadena de entrada a un objeto LocalDate
            LocalDate fechaIngresada = LocalDate.parse(inputFecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Calcular la fecha de pago de la quincena
            LocalDate fechaPagoQuincena = cfq.calcularFechaPagoQuincena(fechaIngresada);

            // Mostrar la fecha de pago de la quincena
            System.out.println("Fecha de pago de la quincena: " + fechaPagoQuincena.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (Exception e) {
            System.out.println("Error al procesar la entrada. Asegúrese de ingresar la fecha en el formato correcto.");
        } finally {
            // Cerrar el objeto Scanner
            scanner.close();
        }
    }

    public LocalDate calcularFechaPagoQuincena(LocalDate fechaIngresada) {
        // Obtener el día del mes de la fecha ingresada
        int diaDelMes = fechaIngresada.getDayOfMonth();

        // Obtener el día de la semana de la fecha ingresada
        DayOfWeek diaDeLaSemana = fechaIngresada.getDayOfWeek();

        // Calcular la fecha de pago de la quincena
        LocalDate fechaPagoQuincena;
        if (diaDelMes <= 15) {
            // Si el día del mes es 15 o menos, usar el día 15 de ese mes
            fechaPagoQuincena = LocalDate.of(fechaIngresada.getYear(), fechaIngresada.getMonth(), 15);
        } else if (diaDelMes <= fechaIngresada.lengthOfMonth()) {
            // Si el día del mes es entre 16 y 30, usar el día 30 de ese mes
            fechaPagoQuincena = LocalDate.of(fechaIngresada.getYear(), fechaIngresada.getMonth(), fechaIngresada.lengthOfMonth());
        } else {
            // Si el día del mes es 31, usar el día 15 del próximo mes
            fechaPagoQuincena = LocalDate.of(fechaIngresada.getYear(), fechaIngresada.getMonth().plus(1), 15);
        }

        // Ajustar la fecha de pago si es un fin de semana (sábado o domingo)

        diaDeLaSemana = fechaPagoQuincena.getDayOfWeek();

        if (diaDeLaSemana == DayOfWeek.SATURDAY) {
            fechaPagoQuincena = fechaPagoQuincena.minusDays(1); // Retroceder al viernes
        } else if (diaDeLaSemana == DayOfWeek.SUNDAY) {
            fechaPagoQuincena = fechaPagoQuincena.minusDays(2); // Retroceder al viernes
        }

        if (esDiaFestivo(fechaPagoQuincena)) {
            fechaPagoQuincena = retrocederHastaDiaLaborable(fechaPagoQuincena);
        }

        return fechaPagoQuincena;
    }

    private boolean esDiaFestivo(LocalDate fecha) {
        return this.diasFestivos.contains(fecha);
    }

    private LocalDate retrocederHastaDiaLaborable(LocalDate fecha) {
        fecha = fecha.minusDays(1);
        while (fecha.getDayOfWeek() == DayOfWeek.SATURDAY || fecha.getDayOfWeek() == DayOfWeek.SUNDAY || esDiaFestivo(fecha)) {
            fecha = fecha.minusDays(1);
        }
        return fecha;
    }
}
