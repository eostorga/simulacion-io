package Simulacion;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

public class Eventos
{
    double reloj = 0;                   //
    double tiempoTotalSimulacion;       // Tiempo total en segundos para correr cada vez la simulación.
    double tiempo;                      //
    double tiempoToken;                 //
    int vecesSimulacion;                // Número de veces que se va a correr la simulación.
    int filaA;                          //
    int filaB;                          //
    int filaC;                          //
    int filaAntivirus;
    int filaRouter;
    int tamanoArchv;                    //
    int archivosEnviados;               //
    int archivosNoEnviados;             //
    double duracionTotalRevision;       //
    double duracionTransmisionRouter;
    double tiempoTransferencia;         //
    boolean libreAntivirus = true;      //
    boolean enviar = false;             //
    boolean linea1 = true;
    boolean linea2 = true;
    double[] eventos;                   //
    ArrayList<Integer> filaAP1 = new ArrayList<>();
    ArrayList<Integer> filaAP2 = new ArrayList<>();
    ArrayList<Integer> filaBP1 = new ArrayList<>();
    ArrayList<Integer> filaBP2 = new ArrayList<>();
    ArrayList<Integer> filaCP1 = new ArrayList<>();
    ArrayList<Integer> filaCP2 = new ArrayList<>();
    ArrayList<Integer> colaAntivirus = new ArrayList<>();
    Random m_random;
    
    public Eventos()
    {
        eventos = new double[13]; // Número total de eventos.
        /* DEFINICIÓN DE LOS EVENTOS EN EL ARREGLO
         * 
         * [0]  -> Llega de archivo a A
         * [1]  -> Llega de archivo a B
         * [2]  -> Llega de archivo a C
         * [3]  -> A recibe token
         * [4]  -> B recibe token
         * [5]  -> C recibe token
         * [6]  -> A termina de poner en la línea
         * [7]  -> B termina de poner en la línea
         * [8]  -> C termina de poner en la línea
         * [9]  -> Llegada de archivo a antivirus
         * [10] -> Se libera antivirus
         * [11] -> Se libera línea 1
         * [12] -> Se libera línea 2
        */
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
    
    public void inicializarEventos()
    {
        eventos[0] = reloj + proximoArriboA();
        eventos[1] = reloj + proximoArriboB();
        eventos[2] = reloj + proximoArriboC();
        eventos[3] = reloj + 5;
        for(int i = 4; i < 13; i++)
        {
            eventos[i] = Double.MAX_VALUE; // Se inicializan en un número muy grande.
        }
    }
    
    public void iniciarSimulacion()
    {
        int numeroEvento;
        for(int i = 0; i < vecesSimulacion; i++)    // Realiza la simulación la cantidad de veces deseada.
        {
            while(reloj < tiempoTotalSimulacion)    // Durante el tiempo definido por usuario.
            {
                numeroEvento = proximoEvento();     // El próximo evento es el que ocurra más pronto (menor tiempo). 
                if(numeroEvento != -1)              // Solo para asegurarse que haya algún evento.
                {
                    switch(numeroEvento)
                    {
                        case 0: llegaArchivoA(eventos[0]);
                                break;
                        case 1: llegaArchivoB(eventos[1]);
                                break;
                        case 2: llegaArchivoC(eventos[2]);
                                break;
                        case 3: ARecibeToken(eventos[3]);
                                break;
                        case 4: BRecibeToken(eventos[4]);
                                break;
                        case 5: CRecibeToken(eventos[5]);
                                break;
                        case 6: ATerminaPonerLinea(eventos[6]);
                                break;
                        case 7: BTerminaPonerLinea(eventos[7]);
                                break;
                        case 8: CTerminaPonerLinea(eventos[8]);
                                break;
                        case 9: llegadaAntivirus(eventos[9]);       // Falta ver como mandarle el tamaño del archivo.
                                break;
                        case 10: liberaAntivirus(eventos[10], 1);   // Falta ver como mandarle el tamaño del archivo.
                                break;
                        case 11: liberaLinea1(eventos[11], 1);      // Falta ver como mandarle el tamaño del archivo.
                                break;
                        case 12: liberaLinea2(eventos[12], 1);      // Falta ver como mandarle el tamaño del archivo.
                                break;
                    }
                }
                else 
                {
                    // Ya no hay más eventos a realizarse, hay que terminar.
                    reloj = tiempoTotalSimulacion;
                }
            }
        }
    }
    
    public int proximoEvento()
    {
        double menor = Double.MAX_VALUE;  // Un número muy grande cualquiera.
        int indice = -1;                // Un índice no existente.
        for(int i = 0; i < eventos.length; i++)
        {
            if ( eventos[i] < menor)
            {
                menor = eventos[i];  // Devuelve el índice en el arreglo del evento.
                indice = i;
            }
        }
        return indice;
    }
    
    /************************** EVENTOS *************************************/
    
    public void llegaArchivoA(double horaEvento)
    {
        reloj = horaEvento;
        filaA++;
        int prioridad = asignarPrioridad();      // Variable aleatoria discreta.
        int tamano = asignarTamano();         // Variable aleatoria discreta (1-64).        
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
        eventos[0] = reloj + proximoArriboA();// Próxima llegada de archivo a A.
    }
    
    public void llegaArchivoB(double horaEvento)
    {
        reloj = horaEvento;
        filaB++;
        int prioridad = asignarPrioridad();      // Variable aleatoria discreta.
        int tamano = asignarTamano();         // Variable aleatoria discreta (1-64).        
        if(prioridad == 1)
        {
            filaBP1.add(tamano);
            // Ordenar arreglo por tamaño
        }
        else
        {
            filaBP2.add(tamano);
            // Ordenar arreglo por tamaño
        }        
        eventos[1] = reloj + proximoArriboB();// Próxima llegada de archivo a B.
    }
    
    public void llegaArchivoC(double horaEvento)
    {
        reloj = horaEvento;
        filaC++;
        int prioridad = asignarPrioridad();      // Variable aleatoria discreta.
        int tamano = asignarTamano();          // Variable aleatoria uniforme discreta (1-64).        
        if(prioridad == 1)
        {
            filaCP1.add(tamano);
            // Ordenar arreglo por tamaño
        }
        else
        {
            filaCP2.add(tamano);
            // Ordenar arreglo por tamaño
        }        
        eventos[2] = reloj + proximoArriboC();// Próxima llegada de archivo a C.
    }
    
    public void ARecibeToken(double horaEvento)
    {
        reloj = horaEvento;
        tiempo = tiempoToken;
        tiempoTransferencia = 0;
        int i = 0;
        
        if(filaA != 0)
        {
            if(filaAP1.size() > 0)
            {
                do
                {    
                    tiempoTransferencia = filaAP1.get(i) * 0.25;
                    i++;
                }while(i < filaAP1.size() && tiempoTransferencia > tiempo);
                
                if(tiempoTransferencia <= tiempo) // Si sale del ciclo porque va a enviar un archivo
                {
                    filaA -= 1;
                    filaAP1.remove(i-1);
                    /* A termina poner en línea */
                    eventos[6] = reloj + tiempoTransferencia;
                    //tiempo = tiempo - tiempoTransferencia;
                }
                
                if(i >= filaAP1.size()) // Se acabó fila de prioridad 1
                {
                    i = 0;
                    if(filaAP2.size() > 0)
                    {
                        do
                        {    
                            tiempoTransferencia = filaAP2.get(i) * 0.25;
                            i++;
                        }while(i < filaAP2.size() && tiempoTransferencia > tiempo);

                        if(tiempoTransferencia <= tiempo) // Si sale del ciclo porque va a enviar un archivo
                        {
                            filaA -= 1;
                            filaAP2.remove(i-1);
                            /* A termina poner en línea */
                            eventos[6] = reloj + tiempoTransferencia;
                            //tiempo = tiempo - tiempoTransferencia;
                        }

                        if(i >= filaAP2.size())
                        {
                            /* B recibe token */
                            eventos[4] = reloj;
                        }
                    }
                    else
                    {
                        /* B recibe token */
                        eventos[4] = reloj;
                    }
                }
            }
            else
            {
                i = 0;
                if(filaAP2.size() > 0)
                {
                    do
                    {    
                        tiempoTransferencia = filaAP2.get(i) * 0.25;
                        i++;
                    }while(i < filaAP2.size() && tiempoTransferencia > tiempo);

                    if(tiempoTransferencia <= tiempo) // Si sale del ciclo porque va a enviar un archivo
                    {
                        filaA -= 1;
                        filaAP2.remove(i-1);
                        /* A termina poner en línea */
                        eventos[6] = reloj + tiempoTransferencia;
                        //tiempo = tiempo - tiempoTransferencia;
                    }
                    if(i >= filaAP2.size())
                    {
                        /* B recibe token */
                        eventos[4] = reloj;
                    }
                }
                else
                {
                    /* B recibe token */
                    eventos[4] = reloj;
                }
            }
        }
        else
        {
            /* B recibe token */
            eventos[4] = reloj;
        }
        
        // A recibe token = infinito;
        eventos[3] = Double.MAX_VALUE;
    }
    
    public void BRecibeToken(double horaEvento)
    {
        reloj = horaEvento;
        tiempo = tiempoToken;
        tiempoTransferencia = 0;
        int i = 0;
        
        if(filaB != 0)
        {
            if(filaBP1.size() > 0)
            {
                do
                {    
                    tiempoTransferencia = filaBP1.get(i) * 0.25;
                    i++;
                }while(i < filaBP1.size() && tiempoTransferencia > tiempo);
                
                if(tiempoTransferencia <= tiempo) // Si sale del ciclo porque va a enviar un archivo
                {
                    filaB -= 1;
                    filaBP1.remove(i-1);
                    /* B termina poner en línea */
                    eventos[7] = reloj + tiempoTransferencia;
                    //tiempo = tiempo - tiempoTransferencia;
                }
                
                if(i >= filaBP1.size()) // Se acabó fila de prioridad 1
                {
                    i = 0;
                    if(filaBP2.size() > 0)
                    {
                        do
                        {    
                            tiempoTransferencia = filaBP2.get(i) * 0.25;
                            i++;
                        }while(i < filaBP2.size() && tiempoTransferencia > tiempo);

                        if(tiempoTransferencia <= tiempo) // Si sale del ciclo porque va a enviar un archivo
                        {
                            filaB -= 1;
                            filaBP2.remove(i-1);
                            /* B termina poner en línea */
                            eventos[7] = reloj + tiempoTransferencia;
                            //tiempo = tiempo - tiempoTransferencia;
                        }
                        if(i >= filaBP2.size())
                        {
                            /* C recibe token */
                            eventos[5] = reloj;
                        }
                    }
                    else
                    {
                        /* C recibe token */
                        eventos[5] = reloj;
                    }
                }
            }
            else
            {
                i = 0;
                if(filaBP2.size() > 0)
                {
                    do
                    {    
                        tiempoTransferencia = filaBP2.get(i) * 0.25;
                        i++;
                    }while(i < filaBP2.size() && tiempoTransferencia > tiempo);

                    if(tiempoTransferencia <= tiempo) // Si sale del ciclo porque va a enviar un archivo
                    {
                        filaB -= 1;
                        filaBP2.remove(i-1);
                        /* B termina poner en línea */
                        eventos[7] = reloj + tiempoTransferencia;
                        //tiempo = tiempo - tiempoTransferencia;
                    }
                    if(i >= filaBP2.size())
                    {
                        /* C recibe token */
                        eventos[5] = reloj;
                    }
                }
                else
                {
                    /* C recibe token */
                    eventos[5] = reloj;
                }
            }
        }
        else
        {
            /* C recibe token */
            eventos[5] = reloj;
        }
        
        // B recibe token = infinito;
        eventos[4] = Double.MAX_VALUE;
    }
    
    public void CRecibeToken(double horaEvento)
    {
        reloj = horaEvento;
        tiempo = tiempoToken;
        tiempoTransferencia = 0;
        int i = 0;
        
        if(filaC != 0)
        {
            if(filaCP1.size() > 0)
            {
                do
                {    
                    tiempoTransferencia = filaCP1.get(i) * 0.25;
                    i++;
                }while(i < filaCP1.size() && tiempoTransferencia > tiempo);
                
                if(tiempoTransferencia <= tiempo) // Si sale del ciclo porque va a enviar un archivo
                {
                    filaC -= 1;
                    filaCP1.remove(i-1);
                    /* C termina poner en línea */
                    eventos[8] = reloj + tiempoTransferencia;
                    //tiempo = tiempo - tiempoTransferencia;
                }
                
                if(i >= filaCP1.size()) // Se acabó fila de prioridad 1
                {
                    i = 0; 
                    if(filaCP2.size() > 0)
                    {                   
                        do
                        {    
                            tiempoTransferencia = filaCP2.get(i) * 0.25;
                            i++;
                        }while(i < filaCP2.size() && tiempoTransferencia > tiempo);

                        if(tiempoTransferencia <= tiempo) // Si sale del ciclo porque va a enviar un archivo
                        {
                            filaC -= 1;
                            filaCP2.remove(i-1);
                            /* C termina poner en línea */
                            eventos[8] = reloj + tiempoTransferencia;
                            //tiempo = tiempo - tiempoTransferencia;
                        }
                        if(i >= filaCP2.size())
                        {
                            /* A recibe token */
                            eventos[3] = reloj;
                        }
                    }
                    else
                    {
                        /* A recibe token */
                        eventos[3] = reloj;
                    }
                }
            }
            else
            {
                i = 0;
                if(filaCP2.size() > 0)
                {
                    do
                    {    
                        tiempoTransferencia = filaCP2.get(i) * 0.25;
                        i++;
                    }while(i < filaCP2.size() && tiempoTransferencia > tiempo);

                    if(tiempoTransferencia <= tiempo) // Si sale del ciclo porque va a enviar un archivo
                    {
                        filaC -= 1;
                        filaCP2.remove(i-1);
                        /* C termina poner en línea */
                        eventos[8] = reloj + tiempoTransferencia;
                        //tiempo = tiempo - tiempoTransferencia;
                    }
                    if(i >= filaCP2.size())
                    {
                        /* A recibe token */
                        eventos[3] = reloj;
                    }
                }
                else
                {
                    /* A recibe token */
                    eventos[3] = reloj;
                }
            }
        }
        else
        {
            /* A recibe token */
            eventos[3] = reloj;
        }
        
        // C recibe token = infinito;
        eventos[5] = Double.MAX_VALUE;
    }
    
    public void ATerminaPonerLinea(double horaEvento)
    {
        reloj = horaEvento;
        tiempo = tiempo - tiempoTransferencia;
        /* Llegar a antivirus = reloj + 1; */
        eventos[9] = reloj + 1;
        int i = 0;
        
        if(tiempo != 0)
        {
            if(filaA != 0)
            {
                if(filaAP1.size() > 0)
                {
                    do
                    {    
                        tiempoTransferencia = filaAP1.get(i) * 0.25;
                        i++;
                    }while(i < filaAP1.size() && tiempoTransferencia > tiempo);

                    if(tiempoTransferencia <= tiempo)
                    {
                        filaA -= 1;
                        tamanoArchv = filaAP1.get(i-1);
                        filaAP1.remove(i-1);
                        /* A termina poner en línea */
                        eventos[6] = reloj + tiempoTransferencia;
                        tiempo = tiempo - tiempoTransferencia;
                    }
                    
                    if(i >= filaAP1.size()) // Se acabó fila de prioridad 1
                    {
                        i = 0;
                        if(filaAP2.size() > 0)
                        {
                            do
                            {    
                                tiempoTransferencia = filaAP2.get(i) * 0.25;
                                i++;
                            }while(i < filaAP2.size() && tiempoTransferencia > tiempo);

                            if(tiempoTransferencia <= tiempo)
                            {
                                filaA -= 1;
                                tamanoArchv = filaAP2.get(i-1);
                                filaAP2.remove(i-1);
                                /* A termina poner en línea */
                                eventos[6] = reloj + tiempoTransferencia;
                                tiempo = tiempo - tiempoTransferencia;
                            }
                            if(i >= filaAP2.size()) // Se acabó fila de prioridad 2
                            {
                                /* B recibe token */
                                eventos[4] = reloj;
                                /* A termina poner en línea = infinito; */
                                eventos[6] = Double.MAX_VALUE;
                            }
                        }
                        else
                        {
                            /* B recibe token */
                            eventos[4] = reloj;
                            /* A termina poner en línea = infinito; */
                            eventos[6] = Double.MAX_VALUE;
                        }
                    }
                }
                else
                {
                    if(filaAP2.size() > 0)
                    {
                        do
                        {    
                            tiempoTransferencia = filaAP2.get(i) * 0.25;
                            i++;
                        }while(i < filaAP2.size() && tiempoTransferencia > tiempo);

                        if(tiempoTransferencia <= tiempo) // Si sale del ciclo porque va a enviar un archivo
                        {
                            filaA -= 1;
                            tamanoArchv = filaAP2.get(i-1);
                            filaAP2.remove(i-1);
                            /* A termina poner en línea */
                            eventos[6] = reloj + tiempoTransferencia;
                            tiempo = tiempo - tiempoTransferencia;
                        }
                        if(i >= filaAP2.size()) // Se acabó fila de prioridad 2
                        {
                            /* B recibe token */
                            eventos[4] = reloj;
                            /* A termina poner en línea = infinito; */
                            eventos[6] = Double.MAX_VALUE;
                        }
                    }
                    else
                    {
                        /* B recibe token */
                        eventos[4] = reloj;
                        /* A termina poner en línea = infinito; */
                        eventos[6] = Double.MAX_VALUE;
                    }
                }
            }
            else
            {
                /* B recibe token */
                eventos[4] = reloj;
                /* A termina poner en línea = infinito; */
                eventos[6] = Double.MAX_VALUE;
            }
        }
        else
        {
            /* B recibe token */
            eventos[4] = reloj;
            /* A termina poner en línea = infinito; */
            eventos[6] = Double.MAX_VALUE;
        }
    }
    public void BTerminaPonerLinea(double horaEvento)
    {
        reloj = horaEvento;
        tiempo = tiempo - tiempoTransferencia;
        /* Llegar a antivirus = reloj + 1; */
        eventos[9] = reloj + 1;
        int i = 0;
        
        if(tiempo != 0)
        {
            if(filaB != 0)
            {
                if(filaBP1.size() > 0)
                {
                    do
                    {    
                        tiempoTransferencia = filaBP1.get(i) * 0.25;
                        i++;
                    }while(i < filaBP1.size() && tiempoTransferencia > tiempo);

                    if(tiempoTransferencia <= tiempo)
                    {
                        filaB -= 1;
                        tamanoArchv = filaBP1.get(i-1);
                        filaBP1.remove(i-1);
                        /* B termina poner en línea */
                        eventos[7] = reloj + tiempoTransferencia;
                        tiempo = tiempo - tiempoTransferencia;
                    }
                    
                    if(i >= filaBP1.size()) // Se acabó fila de prioridad 1
                    {
                        i = 0;
                        if(filaBP2.size() > 0)
                        {
                            do
                            {    
                                tiempoTransferencia = filaBP2.get(i) * 0.25;
                                i++;
                            }while(i < filaBP2.size() && tiempoTransferencia > tiempo);

                            if(tiempoTransferencia <= tiempo)
                            {
                                filaB -= 1;
                                tamanoArchv = filaBP2.get(i-1);
                                filaBP2.remove(i-1);
                                /* B termina poner en línea */
                                eventos[7] = reloj + tiempoTransferencia;
                                tiempo = tiempo - tiempoTransferencia;
                            }
                            if(i >= filaBP2.size()) // Se acabó fila de prioridad 2
                            {
                                /* C recibe token */
                                eventos[5] = reloj;
                                /* B termina poner en línea = infinito; */
                                eventos[7] = Double.MAX_VALUE;
                            }
                        }
                        else
                        {
                            /* C recibe token */
                            eventos[5] = reloj;
                            /* B termina poner en línea = infinito; */
                            eventos[7] = Double.MAX_VALUE;
                        }
                    }
                }
                else
                {
                    if(filaBP2.size() > 0)
                    {
                        do
                        {    
                            tiempoTransferencia = filaBP2.get(i) * 0.25;
                            i++;
                        }while(i < filaBP2.size() && tiempoTransferencia > tiempo);

                        if(tiempoTransferencia <= tiempo) // Si sale del ciclo porque va a enviar un archivo
                        {
                            filaB -= 1;
                            tamanoArchv = filaBP2.get(i-1);
                            filaBP2.remove(i-1);
                            /* B termina poner en línea */
                            eventos[7] = reloj + tiempoTransferencia;
                            tiempo = tiempo - tiempoTransferencia;
                        }
                        if(i >= filaBP2.size()) // Se acabó fila de prioridad 2
                        {
                            /* C recibe token */
                            eventos[5] = reloj;
                            /* B termina poner en línea = infinito; */
                            eventos[7] = Double.MAX_VALUE;
                        }
                    }
                    else
                    {
                        /* C recibe token */
                        eventos[5] = reloj;
                        /* B termina poner en línea = infinito; */
                        eventos[7] = Double.MAX_VALUE;
                    }
                }
            }
            else
            {
                /* C recibe token */
                eventos[5] = reloj;
                /* B termina poner en línea = infinito; */
                eventos[7] = Double.MAX_VALUE;
            }
        }
        else
        {
            /* C recibe token */
            eventos[5] = reloj;
            /* B termina poner en línea = infinito; */
            eventos[7] = Double.MAX_VALUE;
        }
    }
    
    public void CTerminaPonerLinea(double horaEvento)
    {
        reloj = horaEvento;
        tiempo = tiempo - tiempoTransferencia;
        /* Llegar a antivirus = reloj + 1; */
        eventos[9] = reloj + 1;
        int i = 0;
        
        if(tiempo != 0)
        {
            if(filaC != 0)
            {
                if(filaCP1.size() > 0)
                {
                    do
                    {    
                        tiempoTransferencia = filaCP1.get(i) * 0.25;
                        i++;
                    }while(i < filaCP1.size() && tiempoTransferencia > tiempo);

                    if(tiempoTransferencia <= tiempo)
                    {
                        filaC -= 1;
                        tamanoArchv = filaCP1.get(i-1);
                        filaCP1.remove(i-1);
                        /* C termina poner en línea */
                        eventos[8] = reloj + tiempoTransferencia;
                        tiempo = tiempo - tiempoTransferencia;
                    }
                    
                    if(i >= filaCP1.size()) // Se acabó fila de prioridad 1
                    {
                        i = 0;
                        if(filaCP2.size() > 0)
                        { 
                            do
                            {    
                                tiempoTransferencia = filaCP2.get(i) * 0.25;
                                i++;
                            }while(i < filaCP2.size() && tiempoTransferencia > tiempo);

                            if(tiempoTransferencia <= tiempo)
                            {
                                filaC -= 1;
                                tamanoArchv = filaCP2.get(i-1);
                                filaCP2.remove(i-1);
                                /* C termina poner en línea */
                                eventos[8] = reloj + tiempoTransferencia;
                                tiempo = tiempo - tiempoTransferencia;
                            }
                            if(i >= filaCP2.size()) // Se acabó fila de prioridad 2
                            {
                                /* A recibe token */
                                eventos[3] = reloj;
                                /* C termina poner en línea = infinito; */
                                eventos[8] = Double.MAX_VALUE;
                            }
                        }
                        else
                        {
                            /* A recibe token */
                            eventos[3] = reloj;
                            /* C termina poner en línea = infinito; */
                            eventos[8] = Double.MAX_VALUE;
                        }
                    }
                }
                else
                {
                    if(filaCP2.size() > 0)
                    { 
                        do
                        {    
                            tiempoTransferencia = filaCP2.get(i) * 0.25;
                            i++;
                        }while(i < filaCP2.size() && tiempoTransferencia > tiempo);

                        if(tiempoTransferencia <= tiempo) // Si sale del ciclo porque va a enviar un archivo
                        {
                            filaC -= 1;
                            tamanoArchv = filaCP2.get(i-1);
                            filaCP2.remove(i-1);
                            /* C termina poner en línea */
                            eventos[8] = reloj + tiempoTransferencia;
                            tiempo = tiempo - tiempoTransferencia;
                        }
                        if(i >= filaCP2.size()) // Se acabó fila de prioridad 2
                        {
                            /* A recibe token */
                            eventos[3] = reloj;
                            /* C termina poner en línea = infinito; */
                            eventos[8] = Double.MAX_VALUE;
                        }
                    }
                    else
                    {
                        /* A recibe token */
                        eventos[3] = reloj;
                        /* C termina poner en línea = infinito; */
                        eventos[8] = Double.MAX_VALUE;
                    }
                }
            }
            else
            {
                /* A recibe token */
                eventos[3] = reloj;
                /* C termina poner en línea = infinito; */
                eventos[8] = Double.MAX_VALUE;
            }
        }
        else
        {
            /* A recibe token */
            eventos[3] = reloj;
            /* C termina poner en línea = infinito; */
            eventos[8] = Double.MAX_VALUE;
        }
    }
    
    public void llegadaAntivirus(double horaEvento)
    {
        reloj = horaEvento;
        int seEnvia;

        if(libreAntivirus)
        {
            libreAntivirus = false;
            seEnvia = tieneVirus(); // Variable aleatoria para decidir si se descarta o se envía.
            /* Se genera la bandera de envío y duración total de revisión */
            if(seEnvia == 4)
            {
                enviar = false;
                archivosEnviados++;
                duracionTotalRevision = ((tamanoArchv/8)+(tamanoArchv/16)+(tamanoArchv/24));
            }
            else
            {
                enviar = true;
                archivosNoEnviados++;
                if(seEnvia == 1)
                {
                    duracionTotalRevision = tamanoArchv/8;
                }
                if(seEnvia == 2)
                {
                    duracionTotalRevision = ((tamanoArchv/8)+(tamanoArchv/16));

                }
                if(seEnvia == 3)
                {
                    duracionTotalRevision = ((tamanoArchv/8)+(tamanoArchv/16)+(tamanoArchv/24));
                }
            }
            /* Se libera antivirus = reloj  + duración total */
            //eventos[10] = reloj +duracionTotalRevision;
        }
        else
        {
            filaAntivirus++;
            colaAntivirus.add(tamanoArchv);
        }        
        /* Llegada antivirus = infinito */
        eventos[9] = Double.MAX_VALUE;
    }
    
    public void liberaAntivirus(double horaEvento, int tamanoArchv)
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
    
    public void liberaLinea1(double horaEvento, int tamanoArchv)
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
    public void liberaLinea2(double horaEvento, int tamanoArchv){}

    /************* GENERACIÓN DE NÚMEROS ALEATORIOS ***************************/
    
    /**
     * Asignar 
     */
    
    public int tieneVirus()
    {
        int i = m_random.nextInt(1000000); // Valor entre 0 (incluido) y 1000000 (excluido).
        int j = 0;
        if(i < 950000)
        {
            j = 1;
        }
        if(i >= 950000 && i < 997500)
        {
            j = 2;
        }
        if(i >= 997500 && i < 999875)
        {
            j = 3;
        }
        if(i >= 999875 && i < 1000000)
        {
            j = 4;
        }
        return j;
    }
    
    /**
     * Asigna prioridad a un archivo.
     * Variable aleatoria uniforme: X={1,2}
     */
    public int asignarPrioridad()
    {
        int prioridad = 0;
        int i = m_random.nextInt(10); // Valor entre 0 (incluido) y 10 (excluido).
        if(i < 5)   // 0 <= i < 5
        {
            prioridad = 1;
        }
        else        // 5 <= i < 10
        {
            prioridad = 2;
        }
        return prioridad;
    }
    
    /**
     * Asigna tamaño a un archivo.
     * Variable aleatoria uniforme: X={1,2,3,...,64}
     */
    public int asignarTamano()
    {
        int tamano= 0;
        int i = m_random.nextInt(1000000); // Valor entre 0 (incluido) y 1000000 (excluido).
        // 0 <= i < 15624
        if(i < 15625){ tamano = 1; }
        // 15625 <= i < 31250
        if( i >= 15625 && i < 31250){ tamano = 2; }
        // 31250 <= i < 46875
        if( i >= 31250 && i < 46875){ tamano = 3; }
        if( i >= 46875 && i < 62500){ tamano = 4; }
        if( i >= 62500 && i < 78125){ tamano = 5; }
        if( i >= 78125 && i < 93750){ tamano = 6; }
        if( i >= 93750 && i < 109375){tamano = 7; }
        if( i >= 109375 && i < 125000){ tamano = 8; }
        if( i >= 125000 && i < 140625){ tamano = 9; }
        if( i >= 140625 && i < 156250){ tamano = 10; }
        if( i >= 156250 && i < 171875){ tamano = 11; }
        if( i >= 171875 && i < 187500){ tamano = 12; }
        if( i >= 187500 && i < 203125){ tamano = 13; }
        if( i >= 203125 && i < 218750){ tamano = 14; }
        if( i >= 218750 && i < 234375){ tamano = 15; }
        if( i >= 234375 && i < 250000){ tamano = 16; }
        if( i >= 250000 && i < 265625){ tamano = 17; }
        if( i >= 265625 && i < 281250){ tamano = 18; }
        if( i >= 281250 && i < 296875){ tamano = 19; }
        if( i >= 296875 && i < 312500){ tamano = 20; }
        if( i >= 312500 && i < 328125){ tamano = 21; }
        if( i >= 328125 && i < 343750){ tamano = 22; }
        if( i >= 343750 && i < 359375){ tamano = 23; }
        if( i >= 359375 && i < 375000){ tamano = 24; }
        if( i >= 375000 && i < 390625){ tamano = 25; }
        if( i >= 390625 && i < 406250){ tamano = 26; }
        if( i >= 406250 && i < 421875){ tamano = 27; }
        if( i >= 421875 && i < 437500){ tamano = 28; }
        if( i >= 437500 && i < 453125){ tamano = 29; }
        if( i >= 453125 && i < 468750){ tamano = 30; }
        if( i >= 468750 && i < 484375){ tamano = 31; }
        if( i >= 484375 && i < 500000){ tamano = 32; }
        if( i >= 500000 && i < 15625 + 500000){ tamano = 33; }
        if( i >= 15625 + 500000 && i < 31250 + 500000){ tamano = 34; }
        if( i >= 31250 + 500000 && i < 46875 + 500000){ tamano = 35; }
        if( i >= 46875 + 500000 && i < 62500 + 500000){ tamano = 36; }
        if( i >= 62500 + 500000 && i < 78125 + 500000){ tamano = 37; }
        if( i >= 78125 + 500000 && i < 93750 + 500000){ tamano = 38; }
        if( i >= 93750 + 500000 && i < 109375 + 500000){tamano = 39; }
        if( i >= 109375 + 500000 && i < 125000 + 500000){ tamano = 40; }
        if( i >= 125000 + 500000 && i < 140625 + 500000){ tamano = 41; }
        if( i >= 140625 + 500000 && i < 156250 + 500000){ tamano = 42; }
        if( i >= 156250 + 500000 && i < 171875 + 500000){ tamano = 42; }
        if( i >= 171875 + 500000 && i < 187500 + 500000){ tamano = 44; }
        if( i >= 187500 + 500000 && i < 203125 + 500000){ tamano = 45; }
        if( i >= 203125 + 500000 && i < 218750 + 500000){ tamano = 46; }
        if( i >= 218750 + 500000 && i < 234375 + 500000){ tamano = 47; }
        if( i >= 234375 + 500000 && i < 250000 + 500000){ tamano = 48; }
        if( i >= 250000 + 500000 && i < 265625 + 500000){ tamano = 49; }
        if( i >= 265625 + 500000 && i < 281250 + 500000){ tamano = 50; }
        if( i >= 281250 + 500000 && i < 296875 + 500000){ tamano = 51; }
        if( i >= 296875 + 500000 && i < 312500 + 500000){ tamano = 52; }
        if( i >= 312500 + 500000 && i < 328125 + 500000){ tamano = 53; }
        if( i >= 328125 + 500000 && i < 343750 + 500000){ tamano = 54; }
        if( i >= 343750 + 500000 && i < 359375 + 500000){ tamano = 55; }
        if( i >= 359375 + 500000 && i < 375000 + 500000){ tamano = 56; }
        if( i >= 375000 + 500000 && i < 390625 + 500000){ tamano = 57; }
        if( i >= 390625 + 500000 && i < 406250 + 500000){ tamano = 58; }
        if( i >= 406250 + 500000 && i < 421875 + 500000){ tamano = 59; }
        if( i >= 421875 + 500000 && i < 437500 + 500000){ tamano = 60; }
        if( i >= 437500 + 500000 && i < 453125 + 500000){ tamano = 61; }
        if( i >= 453125 + 500000 && i < 468750 + 500000){ tamano = 62; }
        if( i >= 468750 + 500000 && i < 484375 + 500000){ tamano = 63; }
        if( i >= 484375 + 500000 && i < 1000000){ tamano = 64; }
        return tamano;
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
        int x = m_random.nextInt(13);
        double proximo = ((double)x)/40;
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
        String stringInput;
        
        stringInput = JOptionPane.showInputDialog("Ingrese las veces a recorrer la simulación");
        numeroVeces = Integer.parseInt(stringInput);
        simulacion.setVecesSimulacion(numeroVeces);
        
        stringInput = JOptionPane.showInputDialog("Ingrese el tiempo de la simulación");
        tiempoTotal = Double.parseDouble(stringInput);
        simulacion.setTiempoSimulacion(tiempoTotal);
        
        stringInput = JOptionPane.showInputDialog("Ingrese el tiempo de token");
        tiempoToken = Double.parseDouble(stringInput);
        simulacion.setToken(tiempoToken);
        
        simulacion.iniciarSimulacion();
        System.out.println(simulacion.filaA+" "+simulacion.filaAP1.size()+" "+simulacion.filaAP2.size());
        System.out.println(simulacion.filaB+" "+simulacion.filaBP1.size()+" "+simulacion.filaBP2.size());
        System.out.println(simulacion.filaC+" "+simulacion.filaCP1.size()+" "+simulacion.filaCP2.size());
        System.out.println(simulacion.filaAntivirus);
        System.out.println(simulacion.archivosEnviados);
        System.out.println(simulacion.archivosNoEnviados);
        System.out.println(simulacion.reloj);
    }
}