package com.alejo;

import com.alejo.model.Persona;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class Main {
    public static void main(String[] args) {

        Persona persona1 = new Persona("Juan", "Pérez", "123456789", 30, "Aries");
        Persona persona2 = new Persona("María", "Gómez", "987654321", 25, "Virgo");
        Persona persona3 = new Persona("Carlos", "Martínez", "555444333", 40, "Capricornio");
        Persona persona4 = new Persona("Laura", "Rodríguez", "111222333", 35, "Tauro");
        Persona persona5 = new Persona("Pedro", "Sánchez", "999888777", 28, "Leo");
        Persona persona6 = new Persona("Ana", "Fernández", "666777888", 22, "Acuario");
        Persona persona7 = new Persona("David", "López", "333222111", 45, "Cáncer");
        Persona persona8 = new Persona("Sofía", "Díaz", "777666555", 32, "Géminis");
        Persona persona9 = new Persona("Javier", "Hernández", "888999000", 27, "Escorpio");
        Persona persona10 = new Persona("Elena", "García", "112233445", 33, "Libra");
        Persona persona11 = new Persona("Pablo", "Muñoz", "554433221", 38, "Piscis");
        Persona persona12 = new Persona("Rosa", "Jiménez", "998877665", 29, "Sagitario");

        List<Persona> personas = List.of(persona1,persona2,persona3,persona4,persona5,persona6,persona7,persona8,
                persona9,persona10,persona11,persona12);

        //Primero Creación de Flux.
        Flux<Persona> personaFlux = Flux.fromIterable(personas);
        personaFlux.subscribe(System.out::println);

        //Segundo
        System.out.println();
        System.out.println("Mayores de 30 años");
        personaFlux.filter(persona -> persona.getEdad() > 30)
                .subscribe(System.out::println);

        //TErcero
        System.out.println();
        System.out.println("Mostrar los nombres de las personas mayores de 30 años utilizando map(), subscribe() y filter()");
        personaFlux.filter(persona -> persona.getEdad() > 30)
                .map(persona -> persona.getNombre())
                .subscribe(System.out::println);

        //Cuarto
        System.out.println();
        System.out.println("Crear un Mono con la primera persona de la lista.");
        Mono<Persona> personaMono = Mono.just(personas.get(0));
        personaMono.subscribe(System.out::println);

        //Quinto
        System.out.println();
        System.out.println("Mostrar el nombre y apellido de la persona del Mono utilizando flatMap() y subscribe().");
        personaMono.flatMap(persona -> Mono.just(persona.getNombre() + " " + persona.getApellido()))
                .subscribe(System.out::println);

        //Sexto
        System.out.println();
        System.out.println("Agrupar a las personas por signo del zodiaco utilizando groupBy(), flatMap() y collectList(). Luego, mostrar el signo y la cantidad de personas para cada grupo.");
        personaFlux
                .groupBy(Persona::getSigno) // Agrupamos por signo del zodiaco
                .flatMap(groupedFlux ->
                        groupedFlux.collectList().map(personList ->
                                Map.entry(groupedFlux.key(), personList.size()) // Creamos un Map.Entry con el signo y la cantidad de personas
                        )
                )
                .subscribe(entry ->
                        System.out.println("Signo: " + entry.getKey() + ", Cantidad de personas: " + entry.getValue())
                );

        //Septimo
        System.out.println();
        System.out.println("Crear una función obtenerPersonasPorEdad(int edad) que reciba una edad como parámetro y devuelva un Flux con las personas que tengan esa edad.");

        Function<Integer, Flux<Persona>> obtenerPersonasPorEdad =
                edad -> Flux.fromIterable(personas)
                        .filter(persona -> persona.getEdad() == edad);

        obtenerPersonasPorEdad.apply(30).subscribe(System.out::println);

        //Octavo
        System.out.println();
        System.out.println("Crear una función obtenerPersonasPorSigno(String signo) que reciba un signo del zodiaco como parámetro y devuelva un Flux con las personas que tengan ese signo.");
        Function<String, Flux<Persona>> obtenerPersonasPorSigno =
                signo -> Flux.fromIterable(personas)
                        .filter(persona -> persona.getSigno().equals(signo));
        obtenerPersonasPorSigno.apply("Aries").subscribe(System.out::println);


        //Noveno
        System.out.println();
        System.out.println("Crear una función obtenerPersonaPorTelefono(String telefono) que reciba un número de teléfono como parámetro y devuelva un Mono con la persona que tenga ese número de teléfono. Si no se encuentra, devolver un Mono vacío.");
        Function<String, Flux<Persona>> obtenerPersonasPorTelefono =
                telefono -> Flux.fromIterable(personas)
                        .filter(persona -> persona.getTelefono().equals(telefono))
                        .switchIfEmpty(Mono.empty());
        obtenerPersonasPorTelefono.apply("123456789").subscribe(System.out::println);
    }


}
