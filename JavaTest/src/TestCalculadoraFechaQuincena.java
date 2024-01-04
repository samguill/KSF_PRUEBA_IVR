import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class TestCalculadoraFechaQuincena {

    @Test
    public void test30042023() {
        CalculadoraFechaQuincena cfq = new CalculadoraFechaQuincena();
        String str = "2023-04-30";
        LocalDate fechaIngresada = LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate fechaPagoQuincena = cfq.calcularFechaPagoQuincena(fechaIngresada);

        assertEquals("2023-04-28", fechaPagoQuincena.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Test
    public void test30072023() {
        CalculadoraFechaQuincena cfq = new CalculadoraFechaQuincena();
        String str = "2023-07-30";
        LocalDate fechaIngresada = LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate fechaPagoQuincena = cfq.calcularFechaPagoQuincena(fechaIngresada);

        assertEquals("2023-07-31", fechaPagoQuincena.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Test
    public void test15062023() {
        CalculadoraFechaQuincena cfq = new CalculadoraFechaQuincena();
        String str = "2023-06-15";
        LocalDate fechaIngresada = LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate fechaPagoQuincena = cfq.calcularFechaPagoQuincena(fechaIngresada);

        assertEquals(str, fechaPagoQuincena.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Test
    public void testdiafestivo() {

        List<LocalDate> diasFestivos = new ArrayList<>();
        diasFestivos.add(LocalDate.of(2023, 7, 31));
        diasFestivos.add(LocalDate.of(2024, 1, 1));   // Año Nuevo

        CalculadoraFechaQuincena cfq = new CalculadoraFechaQuincena();
        cfq.diasFestivos = diasFestivos;

        String str = "2023-07-31";
        LocalDate fechaIngresada = LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate fechaPagoQuincena = cfq.calcularFechaPagoQuincena(fechaIngresada);

        assertEquals("2023-07-28", fechaPagoQuincena.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

}
