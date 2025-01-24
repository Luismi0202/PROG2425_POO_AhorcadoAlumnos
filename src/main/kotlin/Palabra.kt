package es.iesra.prog2425_ahorcado

import kotlinx.coroutines.runBlocking
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*

class Palabra(
    var palabraOculta: String
) {
    init{
        añadir_guiones()
    }

    var progreso: Array<Char?> = arrayOfNulls(palabraOculta.trim().length)

    private fun añadir_guiones(){
        for(i in 0..progreso.size){
            progreso[i] = '_'
        }
    }

    fun revelarLetra(letra:Char):Boolean{
        var contador = 0
        var letraEncontrada = false
        for(caracter in palabraOculta){
            if(caracter == letra){
                progreso[contador] = letra
                letraEncontrada = true
            }
            contador++
        }
        return letraEncontrada
    }

    fun obtenerProgreso():String{
        return progreso.joinToString(" ")
    }

    fun esCompleta():Boolean{
        var completa = true
        for(caracter in progreso){
            if(caracter == '_'){
                completa = false
            }
        }
        return completa
    }

    companion object{
        fun generarPalabras(cantidad: Int, tamanioMin: Int, tamanioMax: Int, idioma: Idioma = Idioma.ES): MutableSet<Palabra> {
            val client = HttpClient {
                install(ContentNegotiation) {
                    gson()
                }
            }

            val palabras = mutableSetOf<Palabra>() // Usamos un conjunto para evitar repeticiones
            val url = "https://random-word-api.herokuapp.com/word?number=${cantidad * 10}&lang=${idioma.codigo}"

            val patron = if (idioma == Idioma.ES) {
                "^[a-záéíóúüñ]+$"
            } else {
                "^[a-z]+$"
            }

            runBlocking {
                try {
                    while (palabras.size < cantidad) {
                        // Hacemos la solicitud GET
                        val respuesta: Array<String> = client.get(url).body()

                        // Filtramos las palabras según las condiciones
                        val filtradas = respuesta
                            .map { it.trim().lowercase() } // Convertimos a minúsculas
                            .filter { it.length in tamanioMin..tamanioMax } // Filtramos por tamaño
                            .filter { it.matches(Regex(patron)) } // Solo letras
                            .filter { !it.contains(" ") } // Excluye palabras que contengan espacios
                            .map { Palabra(it) } // Mapeamos a la data class

                        palabras.addAll(filtradas)
                    }
                } catch (e: Exception) {
                    println("Error al obtener las palabras: ${e.message}")
                }
            }

            client.close()
            return palabras
        }
    }
}