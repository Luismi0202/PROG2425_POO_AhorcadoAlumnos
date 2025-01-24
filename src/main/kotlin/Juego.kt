package es.iesra.prog2425_ahorcado

class Juego(
    var palabra:Palabra,
    var jugador: Jugador
) {
    fun inicio(){
        println("¡Bienvenido al juego del Ahorcado!")
        println("La palabra tiene ${palabra.palabraOculta.length} letras.")
        do{
            println("Palabra: ${palabra.obtenerProgreso()}")
            println("Intentos restantes: ${jugador.intentos}")
            println("Letras usadas: ${jugador.obtenerLetrasUsadas()}")
            println("Introduce una letra")
            var letra = pedirLetra()
            if(palabra.revelarLetra(letra)){
                println("¡Bien hecho! La letra $letra está en la palabra.")
            }
            else{
                println("La letra $letra no está en la palabra")
                jugador.fallarIntento()
            }

        }while(jugador.intentos > 0 && palabra.esCompleta() == false)
        if (palabra.esCompleta()) {
            println("\n¡Felicidades! Has adivinado la palabra: ${palabra.obtenerProgreso()}")
        } else {
            println("\nLo siento, te has quedado sin intentos. La palabra era: ${palabra.palabraOculta}")
        }
    }

    private fun pedirLetra():Char{
        var letravalida = false
        var letra1 = '_'
        do{
            try{
                var letra2 = readln().lowercase().firstOrNull()
                if(letra2 == null){
                    throw Exception("Letra no válida (nulo).")
                }
                else{
                    if(jugador.intentarLetra(letra2) == false){
                        throw Exception("La letra ya estaba utilizada.")
                    }
                    else{
                        letra1 = letra2
                        letravalida = true
                    }
                }
            }catch(e:Exception){
                println("¡Error! ${e.message}. Intenta otravez")
            }
        }while(!letravalida)
        return letra1
    }

    fun preguntar(msj: String): Boolean {
        do {
            print("$msj (s/n): ")
            val respuesta = readln().trim().lowercase()
            when (respuesta) {
                "s" -> return true
                "n" -> return false
                else -> println("Respuesta no válida! Inténtelo de nuevo...")
            }
        } while (true)
    }
}