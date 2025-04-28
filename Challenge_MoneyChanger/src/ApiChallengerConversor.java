import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApiChallengerConversor {
    private static JsonObject conversionRates;

    public static void main(String[] args) {
        // Obtener las tasas de cambio
        obtenerTasasDeCambio();

        if (conversionRates != null) {
            mostrarMenu();
        }
    }

    /**
     * Método para mostrar el menu con sus respectivas opciones
     */
    private static void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("\n|****************************|");
            System.out.println("|  Bienvenid@ al             |");
            System.out.println("|    Conversor de Moneda 2.0 |");
            System.out.println("|****************************|");
            System.out.println("| 1) USD a EUR               |");
            System.out.println("| 2) USD a GBP               |");
            System.out.println("| 3) USD a JPY               |");
            System.out.println("| 4) EUR a USD               |");
            System.out.println("| 5) Salir                   |");
            System.out.println("|----------------------------|");
            System.out.print("Ingresa una opcion valida: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                switch (opcion) {
                    case 1:
                        realizarConversion("USD", "EUR", scanner);
                        break;
                    case 2:
                        realizarConversion("USD", "GBP", scanner);
                        break;
                    case 3:
                        realizarConversion("USD", "JPY", scanner);
                        break;
                    case 4:
                        realizarConversion("EUR", "USD", scanner);
                        break;
                    case 5:
                        System.out.println("Gracias por usar el conversor. ¡Hasta la vista!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } else {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.next();
            }

        } while (opcion != 5);

        scanner.close();
    }

    /**
     * Metodo para obtener las tasas de cambio desde la API , llamado
     */
    private static void obtenerTasasDeCambio() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://v6.exchangerate-api.com/v6/f6eb59388f487dd735e391e9/latest/USD"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

            if ("success".equals(jsonObject.get("result").getAsString())) {
                conversionRates = jsonObject.getAsJsonObject("conversion_rates");
                System.out.println("Tasas de cambio obtenidas correctamente, APIs.\n");
            } else {
                System.out.println("Error al obtener tasas de cambio, intente nuevamente.");
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error al realizar la solicitud o al procesar tu respuesta:");
            e.printStackTrace();
        }
    }

    /**
     * Metodo para realizar la conversión entre dos monedas
     */
    private static void realizarConversion(String monedaOrigen, String monedaDestino, Scanner scanner) {
        System.out.print("Ingrese el monto a convertir de " + monedaOrigen + " a " + monedaDestino + ": ");
        if (scanner.hasNextDouble()) {
            double monto = scanner.nextDouble();
            double montoConvertido = convertir(monedaOrigen, monedaDestino, monto);

            if (montoConvertido != -1) {
                System.out.printf("Resultado: %.2f %s = %.2f %s%n",
                        monto, monedaOrigen, montoConvertido, monedaDestino);
            } else {
                System.out.println("No se pudo realizar la conversión. Monedas no encontradas.");
            }
        } else {
            System.out.println("Entrada inválida. Debe ser un número.");
            scanner.next(); // Consumir entrada inválida
        }
    }

    /**
     * Metodo para calcular la conversión
     */
    private static double convertir(String origen, String destino, double monto) {
        if (conversionRates.has(origen) && conversionRates.has(destino)) {
            double tasaOrigen = conversionRates.get(origen).getAsDouble();
            double tasaDestino = conversionRates.get(destino).getAsDouble();

            double montoEnUSD = monto / tasaOrigen;
            double montoConvertido = montoEnUSD * tasaDestino;

            return montoConvertido;
        } else {
            return -1; // Eror
        }
    }
}

