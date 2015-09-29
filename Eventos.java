package Simulacion;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

public class Eventos
{
    double reloj;
    double tiempoTotalSimulacion;       // Tiempo total en segundos para correr cada vez la simulación.
    double tiempo;
    double tiempoToken;
    int vecesSimulacion;                // Número de veces que se va a correr la simulación.
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
    double[] eventos;
    ArrayList<Integer> filaAP1 = new ArrayList<>();
    ArrayList<Integer> filaAP2 = new ArrayList<>();
    ArrayList<Integer> filaBP1 = new ArrayList<>();
    ArrayList<Integer> filaBP2 = new ArrayList<>();
    ArrayList<Integer> filaCP1 = new ArrayList<>();
    ArrayList<Integer> filaCP2 = new ArrayList<>();
    ArrayList<Integer> colaAntivirus = new ArrayList<>();
    Random m_random;
    
    /* DEFINICIÓN DE LOS EVENTOS EN EL ARREGLO
     * 
     * [0] -> Llega Archivo A
     */
    
    public Eventos()
    {
        eventos = new double[13]; // Número total de eventos.
        m_random = new Random();
        inicializarEventos();
    }
    
    public void setToken(double tiempoUsoToken)
    {
        tiempoToken = tiempoUsoToken;
    }
    
    public void setTiempoSimulacion(double tiempoSimulacion)
    {
        tiempoTotalSimulacion = tiempoSimulacion;
    }
    
    public void setVecesSimulacion(int veces)
    {
        vecesSimulacion = veces;
    }
    
    public double getToken()
    {
        return tiempoToken;
    }
    
    public void inicializarEventos()
    {
        eventos[0] = proximoArriboA();
        eventos[1] = proximoArriboB();
        eventos[2] = proximoArriboC();
        for(int i = 3; i<13; i++)
        {
            eventos[i] = 0;
        }
    }
    
    public void iniciarSimulacion()
    {
        int numeroEvento = 0;
        
        for(int i = 0; i < vecesSimulacion; i++)    // Realiza la simulación la cantidad de veces deseada.
        {
            while(reloj < tiempoTotalSimulacion)    // Durante el tiempo definido por usuario.
            {
                numeroEvento = proximoEvento();     // El próximo evento es el que ocurra más pronto.
                
                switch(numeroEvento)
                {
                    case 0: ;
                        break;
                }
            }
        }
    }
    
    public int proximoEvento()
    {
        int menor = 999999; // Un número muy grande cualquiera.
        for(int i = 0; i < eventos.length; i++)
        {
            if ( eventos[i] < menor)
            {
                menor = i;  // Devuelve el índice en el arreglo del evento.
            }
        }
        return menor;
    }
    
    /************************** EVENTOS *************************************/
    
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

    /**
     * Numeros aleatorios con distribucion exponencial con media de 5 segundos
     */
    public double proximoArriboA(){
        //CREO QUE ESTE ESTA BIEN ASI TENGO QUE VERIFICARLO
        int lambda = 5;
        double x, r;
        r = m_random.nextDouble();
        x = ((-Math.log(1-r))/lambda);
        return x;
    }


    /**
     * Numeros aleatorios con distribucion f(x)=x/40 8<=x<=12
     * ESTE ES EL QUE ME  HACE FALTA VER COMO SE HACE RICARDO
     */
    public double proximoArriboB()
    {
        double proximo = 0;
        return proximo;
    }

    /**
     * Numeros aleatorios con distibición normal con media 5 y varianza 1 
     */
    public double proximoArriboC()
    {
       double x, z, r1, r2;
       r1 = m_random.nextDouble();
       r2 = m_random.nextDouble();
       z=(Math.sqrt(-2*Math.log(r1)))*Math.sin(2*Math.PI*r2);
       x=1*z+5;
       return x;
    }
    
    public static void main (String args [])
    {
        Eventos simulacion;
        int numeroVeces;            // Número de veces que se va a correr la simulación.
        double tiempoTotal;         // Tiempo total en segundos para correr cada vez la simulación.
        boolean modoLento = false;  // Si desea ver la simulación correr en modo lento o no.
        double tiempoToken;         // El tiempo durante el cuál a cada máquina se le asigna el token.
        
        simulacion = new Eventos();
        
        String stringInput = JOptionPane.showInputDialog("Ingrese el tiempo de token");
        int number = Integer.parseInt(stringInput);
        
        simulacion.setToken(number);
        String mensaje = Double.toString(simulacion.getToken());
        JOptionPane.showMessageDialog(null, mensaje);
    }
}
