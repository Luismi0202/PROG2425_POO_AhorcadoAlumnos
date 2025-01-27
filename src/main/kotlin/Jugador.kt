package es.iesra.prog2425_ahorcado

class Jugador(intentosMaximos: Int) {
    var intentos = intentosMaximos

    private var letrasUsadas: MutableSet<Char> = mutableSetOf()

    fun intentarLetra(letraIntentada: Char):Boolean{
        var agregada = false
        if(letraIntentada.quitarAcentos() !in letrasUsadas){
            letrasUsadas.add(letraIntentada)
            agregada = true
        }
        return agregada
    }

    fun fallarIntento(){
        intentos--
    }

    fun obtenerLetrasUsadas():String{
        return letrasUsadas.joinToString(" ")
    }


}