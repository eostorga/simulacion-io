package Simulacion;
import java.util.ArrayList;

public class Eventos
{
    int reloj;
    int tiempo;
    int tiempoToken;
    int filaA;
    int filaB;
    int filaC;
    int filaAntivirus;
    int filaRouter;
    int prioridad;
    int tamano;
    int duracionTotalRevision;
    int duracionTransmisionRouter;
    double tiempoTransferencia;
    boolean libreAntivirus = true;
    boolean enviar = false;
    boolean linea1 = true;
    boolean linea2 = true;
    ArrayList<Integer> filaAP1 = new ArrayList<>();
    ArrayList<Integer> filaAP2 = new ArrayList<>();
    ArrayList<Integer> filaBP1 = new ArrayList<>();
    ArrayList<Integer> filaBP2 = new ArrayList<>();
    ArrayList<Integer> filaCP1 = new ArrayList<>();
    ArrayList<Integer> filaCP2 = new ArrayList<>();
    ArrayList<Integer> colaAntivirus = new ArrayList<>();
    
    public void llegaArchivoA(int horaEvento)
    {
        reloj = horaEvento;
        filaA += 1;
        prioridad = 1;
        tamano = 1;
        
        if(prioridad == 1)
        {
            filaAP1.add(tamano);
            // Ordenar arreglo por tamaño
        }
        else
        {
            filaAP2.add(tamano);
            // Ordenar arreglo por tamaño
        }
        
        // Llega archivo a A = v.a.
    }
    
    public void ARecibeToken(int horaEvento)
    {
        reloj = horaEvento;
        tiempo = tiempoToken;
        tiempoTransferencia = 0;
        int i = 0;
        
        if(filaA != 0)
        {
            if(filaAP1.size() > 0)
            {
                while(i <= filaAP1.size() && tiempoTransferencia == 0)
                {    
                    tiempoTransferencia = filaAP1.get(i) * 0.5;
                    i++;
                }
                
                if(tiempoTransferencia != 0) // Si sale del ciclo porque va a enviar un archivo
                {
                    filaA --;
                    filaAP1.remove(i-1);
                    /* A termina poner en línea = reloj + tiempoTransferencia; */
                }
                else
                {
                    i = 0;
                    while(i <= filaAP2.size() && tiempoTransferencia == 0)
                    {    
                        tiempoTransferencia = filaAP2.get(i) * 0.5;
                        i++;
                    }

                    if(tiempoTransferencia != 0) // Si sale del ciclo porque va a enviar un archivo
                    {
                        filaA --;
                        filaAP2.remove(i-1);
                        /* A termina poner en línea = reloj + tiempoTransferencia; */
                    }
                    else
                    {
                        /* B recibe token = reloj; */
                    }
                }
            }
            else
            {
                while(i <= filaAP2.size() && tiempoTransferencia == 0)
                {    
                    tiempoTransferencia = filaAP2.get(i) * 0.5;
                    i++;
                }

                if(tiempoTransferencia != 0) // Si sale del ciclo porque va a enviar un archivo
                {
                    filaA --;
                    filaAP2.remove(i-1);
                    /* A termina poner en línea = reloj + tiempoTransferencia; */
                }
                else
                {
                    /* B recibe token = reloj; */
                }
            }
        }
        else
        {
            /* B recibe token = reloj; */
        }
        
        // A recibe token = infinito;
    }
    
    public void ATerminaPonerLinea(int horaEvento)
    {
        reloj = horaEvento;
        /* Llegar a antivirus = reloj + 1; */
        int i = 0;
        
        if(tiempo != 0)
        {
            if(filaA != 0)
            {
                if(filaAP1.size() > 0)
                {
                    while(i <= filaAP1.size() && tiempoTransferencia == 0)
                    {    
                        tiempoTransferencia = filaAP1.get(i) * 0.5;
                        i++;
                    }

                    if(tiempoTransferencia != 0) // Si sale del ciclo porque va a enviar un archivo
                    {
                        filaA --;
                        filaAP1.remove(i-1);
                        /* A termina poner en línea = reloj + tiempoTransferencia; */
                    }
                    else
                    {
                        i = 0;
                        while(i <= filaAP2.size() && tiempoTransferencia == 0)
                        {    
                            tiempoTransferencia = filaAP2.get(i) * 0.5;
                            i++;
                        }

                        if(tiempoTransferencia != 0) // Si sale del ciclo porque va a enviar un archivo
                        {
                            filaA --;
                            filaAP2.remove(i-1);
                            /* A termina poner en línea = reloj + tiempoTransferencia; */
                        }
                        else
                        {
                            /* B recibe token = reloj; */
                            /* A termina poner en línea = infinito; */
                        }
                    }
                }
                else
                {
                    while(i <= filaAP2.size() && tiempoTransferencia == 0)
                    {    
                        tiempoTransferencia = filaAP2.get(i) * 0.5;
                        i++;
                    }

                    if(tiempoTransferencia != 0) // Si sale del ciclo porque va a enviar un archivo
                    {
                        filaA --;
                        filaAP2.remove(i-1);
                        /* A termina poner en línea = reloj + tiempoTransferencia; */
                    }
                    else
                    {
                        /* B recibe token = reloj; */
                        /* A termina poner en línea = infinito; */
                    }
                }
            }
            else
            {
                /* B recibe token = reloj; */
                /* A termina poner en línea = infinito; */
            }
        }
        else
        {
            /* B recibe token = reloj; */
            /* A termina poner en línea = infinito; */
        }
    }
    
    public void llegadaAntivirus(int horaEvento, int tamanoArchv)
    {
        reloj = horaEvento;
        
        if(libreAntivirus)
        {
            libreAntivirus = false;
            int vaEnvio = 1; // Variable aleatoria para decidir si se descarta o se envía
            /* Se genera la bandera de envío y duración total de revisión */
            /* Se libera antivirus = reloj  + duración total */
        }
        else
        {
            filaAntivirus++;
            colaAntivirus.add(tamanoArchv);
        }        
        /* Llegada antivirus = infinito */
    }
    
    public void liberaAntivirus(int horaEvento, int tamanoArchv)
    {
        reloj = horaEvento;
        
        if(enviar)
        {
            if(linea1 && linea2)
            {
                int linea = 1/* Escoger cuál línea agarra */;
                if(linea == 1)
                {
                    linea1 = false;
                    duracionTransmisionRouter = tamanoArchv / 64;
                    /* Se libera línea 1 = reloj + duracionTransmisionRouter */
                }
                else
                {
                    linea2 = false;
                    duracionTransmisionRouter = tamanoArchv / 64;
                    /* Se libera línea 2 = reloj + duracionTransmisionRouter */
                }
            }
            else
            {
                if(linea1)
                {
                    linea1 = false;
                    duracionTransmisionRouter = tamanoArchv / 64;
                    /* Se libera línea 1 = reloj + duracionTransmisionRouter */
                }
                else
                {
                    if(linea2)
                    {
                        linea2 = false;
                        duracionTransmisionRouter = tamanoArchv / 64;
                        /* Se libera línea 2 = reloj + duracionTransmisionRouter */
                    }
                    else
                    {
                        filaRouter++;
                    }
                }
            }
        }
        else
        {
            // contar archivos descartados
        }
        if(filaAntivirus != 0)
        {
            libreAntivirus = false;
            filaAntivirus--;
            int vaEnvio = 1; // Variable aleatoria para decidir si se descarta o se envía
            /* Se genera la bandera de envío y duración total de revisión */
            /* Se libera antivirus = reloj  + duración total */
        }
        else
        {
            libreAntivirus = true;
            /* Libera antivirus = infinito */
        }        
    }
    
    public void liberaLinea1(int horaEvento, int tamanoArchv)
    {
        reloj = horaEvento;
        
        if(filaRouter != 0)
        {
            filaRouter--;
            linea1 = false;
            duracionTransmisionRouter = tamanoArchv / 64;
            /* Se libera línea 1 = reloj + duracionTransmisionRouter */
        }
        else
        {
            /* Se libera línea 1 = infinito */
            linea1 = true;
        }
    }
}
