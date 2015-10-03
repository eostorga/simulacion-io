package io;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Eventos
{
    /************************ Variables para estadísticas *********************/   
    int archivosEnviados;       // Total de archivos salidos del antivirus.
    int archivosNoEnviados;     // Total de archivo no enviados por el antivirus.
    int enviadosL1;             // Archivos envíados por la línea 1.
    int enviadosL2;             // Archivos envíados por la línea 2.
    // Promedio de archivos enviados por cada turno del token. LISTO.
    double promedioEnviadosToken; // ESTO ES LO QUE HAY QUE MOSTRAR.
    int contadorPorToken;
    double promedioColas [] = new double [4]; //Para promedio de colas al finalizar la simulacion
    double colasFinales[][]; //Guardara las colas de A B C y Antivirus al finalizar simulacion
    ArrayList<Integer> cantPorToken = new ArrayList<>();
    // Número promedio de revisiones del antivirus por archivo. LISTO.
    double promedioRevisiones; // ESTO ES LO QUE HAY QUE MOSTRAR.
    ArrayList<Integer> cantRevArchv = new ArrayList<>();    
    /**************************************************************************/
    double reloj = 0;                   // Reloj de la simulación.
    double tiempoTotalSimulacion;       // Tiempo total en segundos para correr cada vez la simulación.
    double tiempo;                      // Tiempo de token que tiene cada máquina.
    double tiempoToken;                 // El tiempo de token que asigna el usuario.
    int vecesSimulacion;                // Número de veces que se va a correr la simulación.
    int numeroEvento;                   // Identificador del evento que está siendo procesado.
    int filaA;                          // Contador de archivos llegados a A.
    int filaB;                          // Contador de archivos llegados a B.
    int filaC;                          // Contador de archivos llegados a C.
    int filaAntivirus;                  // Contador de la fila del antivirus.
    int filaRouter;                     // Contador de la fila del router.
    int tamanoArchv;                    // Guarda el tamaño del archivo que será enviado al antivirus.
    int tamArchvLibera;                 // Guarda el tamaño del archivo que saldrá del antivirus.
    int tamArchvL1;                     // Guarda el tamaño del archivo que será enviado por la línea 1.
    int tamArchvL2;                     // Guarda el tamaño del archivo que será enviado por la línea 2.
    int tieneToken;                     // Identificador de la máquina que tiene el token en un momento dado.
    double duracionTotalRevision;       // Tiempo que dura la revisión total de un archivo en el antivirus.
    double duracionTransmisionL1;       // Tiempo que dura transmitiéndose un archivo por la línea 1.
    double duracionTransmisionL2;       // Tiempo que dura transmitiéndose un archivo por la línea 2.
    double tiempoTransferencia;         // Tiempo que dura transmitiéndose un archivo al antivirus.
    boolean libreAntivirus = true;      // Estado del antivirus: libre u ocupado.
    boolean enviar = false;             // Bandera para determinar si un archivo se envía desde el antivirus.
    boolean linea1 = true;              // Estado de la línea 1: libre u ocupada.
    boolean linea2 = true;              // Estado de la línea 2: libre u ocupada.
    boolean lento = false;              // Forma de mostrar la simulación.
    double[] eventos;                   // Arreglo principal para almacenar la hora de ocurrencia de los eventos.
    // Estas colas almacenan los tamaños de los archivos.
    ArrayList<Integer> filaAP1 = new ArrayList<>(); // Cola de prioridad 1 de A.
    ArrayList<Integer> filaAP2 = new ArrayList<>(); // Cola de prioridad 2 de A.
    ArrayList<Integer> filaBP1 = new ArrayList<>(); // Cola de prioridad 1 de B.
    ArrayList<Integer> filaBP2 = new ArrayList<>(); // Cola de prioridad 2 de B.
    ArrayList<Integer> filaCP1 = new ArrayList<>(); // Cola de prioridad 1 de C.
    ArrayList<Integer> filaCP2 = new ArrayList<>(); // Cola de prioridad 2 de C.
    ArrayList<Integer> colaAntivirus = new ArrayList<>(); // Cola del antivirus.
    ArrayList<Integer> colaRouter = new ArrayList<>();	  // Cola del router.
    Random m_random;
    Comparator<Integer> comparador = Collections.reverseOrder();
    Salida salida; 
    Estadisticas estadisticas = new Estadisticas();
    
    public Eventos(int vecesI, double tiempoI, double tokenI, boolean lentoI)
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
	// Datos ingresados por el usuario.
        tiempoToken = tokenI;
        tiempoTotalSimulacion = tiempoI;
        vecesSimulacion = vecesI;
        lento = lentoI;
        colasFinales = new double [4][vecesI];
        m_random = new Random();
        inicializarEventos();
    }
    
    public void inicializarEventos()
    {
	// Todas las máquinas reciben un archivo al inicio de la simulación.
        eventos[0] = reloj;
        eventos[1] = reloj;
        eventos[2] = reloj;
        eventos[3] = reloj;
	// Inicializa el resto como "infinitos" (los desprograma).
        for(int i = 4; i < 13; i++)
        {
            eventos[i] = Double.MAX_VALUE; // Se inicializan en un número muy grande.
        }
    
        
        salida = new Salida(this);
    
    
    }
    
    public void iniciarSimulacion()
    {
        if(lento)
        {
            salida.setVisible(true);
        }
        for(int i = 0; i < vecesSimulacion; i++)    // Realiza la simulación la cantidad de veces deseada.
        {
            
        filaA = filaB = filaC = filaAntivirus = filaRouter = 0;
        archivosEnviados = archivosNoEnviados = enviadosL1 = enviadosL2 = 0;
        contadorPorToken =0;
   
        
        cantPorToken.clear();
        cantRevArchv.clear();
        
        filaAP1.clear();
        filaAP2.clear(); 
        filaBP1.clear();
        filaBP2.clear();
        filaCP1.clear();
        filaCP2.clear();
        colaAntivirus.clear();
        colaRouter.clear();
        reloj = 0 ;
            System.out.println("corrida numero "+(i+1));
            
            inicializarEventos();
            while(reloj < tiempoTotalSimulacion)    // Durante el tiempo definido por usuario.
            {
                numeroEvento = proximoEvento();     // El próximo evento es el que ocurra más pronto (menor tiempo). 
                if(numeroEvento != -1)              // Solo para asegurarse que haya algún evento.
                {
                    System.out.println("corrida "+(i+1)+" reloj "+reloj+" evento "+numeroEvento +" FilaA "+filaA+" FilaB "+filaB+" FilaC "+filaC);
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
                                contadorPorToken++; // Para estadísticas
                                break;
                        case 7: BTerminaPonerLinea(eventos[7]);
                                contadorPorToken++; // Para estadísticas
                                break;
                        case 8: CTerminaPonerLinea(eventos[8]);
                                contadorPorToken++; // Para estadísticas
                                break;
                        case 9: llegadaAntivirus(eventos[9]);
                                break;
                        case 10: liberaAntivirus(eventos[10]);
                                break;
                        case 11: liberaLinea1(eventos[11]);
                                break;
                        case 12: liberaLinea2(eventos[12]);
                                break;                        
                    }                   
                }
                else 
                {
                    // Ya no hay más eventos a realizarse, hay que terminar.
                    reloj = tiempoTotalSimulacion;
                }
		// Muestra los datos en la simulación
                try
                {
                    salida.setValores();
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(Eventos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
            // Calcula las estadísticas al final de cada corrida.
            calcularEstadisticas(i);
            if(lento)
            {
                salida.setVisible(false);
            }
        }
        promedioColasFinal(vecesSimulacion);
        estadisticas.mostrarEstadisticas(promedioColas, promedioEnviadosToken, promedioRevisiones);
        estadisticas.setVisible(true);
    }
    
	// El próximo evento a ejecutarse será aquel cuya hora de ocurrencia se la menor de todos los eventos.
    public int proximoEvento()
    {
        double menor = Double.MAX_VALUE;  	// Un número muy grande cualquiera.
        int indice = -1;                	// Un índice no existente.
        for(int i = 0; i < eventos.length; i++)
        {
            if (eventos[i] < menor)
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
        int prioridad = asignarPrioridad();  // Variable aleatoria discreta.
        int tamano = asignarTamano();        // Variable aleatoria discreta (1-64).
        // Archivos de prioridad 1 se agregan a la respectiva cola
		if(prioridad == 1)
        {
            filaAP1.add(tamano);
            // Ordena cola por tamaño de archivo, de mayor a menor
            Collections.sort(filaAP1, comparador);
        }
		// Archivos de prioridad 2 se agregan a la respectiva cola
        else
        {
            filaAP2.add(tamano);
            // Ordena cola por tamaño de archivo, de mayor a menor
            Collections.sort(filaAP2, comparador);
        }        
        eventos[0] = reloj + proximoArriboA();// Próxima llegada de archivo a A.
    }
    
    public void llegaArchivoB(double horaEvento)
    {
        reloj = horaEvento;
        filaB++;
        int prioridad = asignarPrioridad();      // Variable aleatoria discreta.
        int tamano = asignarTamano();         // Variable aleatoria discreta (1-64).        
        // Archivos de prioridad 1 se agregan a la respectiva cola
		if(prioridad == 1)
        {
            filaBP1.add(tamano);
            // Ordena cola por tamaño de archivo, de mayor a menor
            Collections.sort(filaBP1, comparador);
        }
		// Archivos de prioridad 2 se agregan a la respectiva cola
        else
        {
            filaBP2.add(tamano);
            // Ordena cola por tamaño de archivo, de mayor a menor
            Collections.sort(filaBP2, comparador);
        }        
        eventos[1] = reloj + proximoArriboB();// Próxima llegada de archivo a B.
    }
    
    public void llegaArchivoC(double horaEvento)
    {
        reloj = horaEvento;
        filaC++;
        int prioridad = asignarPrioridad();      // Variable aleatoria discreta.
        int tamano = 5;//asignarTamano();          // Variable aleatoria uniforme discreta (1-64).        
        // Archivos de prioridad 1 se agregan a la respectiva cola
		if(prioridad == 1)
        {
            filaCP1.add(tamano);
            // Ordena cola por tamaño de archivo, de mayor a menor
            Collections.sort(filaCP1, comparador);
        }
		// Archivos de prioridad 2 se agregan a la respectiva cola
        else
        {
            filaCP2.add(tamano);
            // Ordena cola por tamaño de archivo, de mayor a menor
            Collections.sort(filaCP2, comparador);
        }        
        eventos[2] = reloj + proximoArriboC();// Próxima llegada de archivo a C.
    }
    
    public void ARecibeToken(double horaEvento)
    {
        reloj = horaEvento;
        tieneToken = 1; // A tiene token.
	/*
	Cada vez que una máquina recibe el token guarda el contador de archivos enviados
	anteriormente en un vector y lo reinicia, para empezar a contar cuántos enviará.
	Y así poder sacar el promedio de archivos enviados cada vez que se tiene el token.	
	*/
        cantPorToken.add(contadorPorToken);
        contadorPorToken = 0;
        tiempo = tiempoToken;
        tiempoTransferencia = 0;
        int i = 0;
        boolean vaEnviar = false;
        
        if(filaA != 0)
        {
            if(filaAP1.size() > 0)
            {
		/** Se calcula el tiempo de transferencia de un archivo en la cola
		* si este se puede enviar en el tiempo de token que se tiene, se escoge
		* y sale del ciclo, si no, se continua con el siguiente.
                * Solo se puede salir del ciclo si un archivo se puede enviar, o sea,
                * su tamaño lo permite en el tiempo disponible.
                * O salir del ciclo porque 
		*/
                do
                {
                    if(i < filaAP1.size())
                    {
                        tiempoTransferencia = filaAP1.get(i) * 0.25;
                    }
                    i++;
                }while(i <= filaAP1.size() && tiempoTransferencia > tiempo);
                
		// Si da tiempo de enviar ese archivo, se saca dse la cola y se envía.
                if(tiempoTransferencia <= tiempo)
                {
                    vaEnviar = true;
                    filaA -= 1;
                    filaAP1.remove(i-1);
                    /* A termina poner en línea */
                    eventos[6] = reloj + tiempoTransferencia;
                }
                else
                {
                    if(!vaEnviar)
                    {
                        i = 0;
                        if(filaAP2.size() > 0)
                        {
                            do
                            {   if(i < filaAP2.size())
                                {
                                    tiempoTransferencia = filaAP2.get(i) * 0.25;
                                }
                                i++;
                            }while(i <= filaAP2.size() && tiempoTransferencia > tiempo);

                            if(tiempoTransferencia <= tiempo)
                            {
                                vaEnviar = true;
                                filaA -= 1;
                                filaAP2.remove(i-1);
                                /* A termina poner en línea */
                                eventos[6] = reloj + tiempoTransferencia;
                            }
                            else
                            {
                                if(!vaEnviar)
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
                        if(i < filaAP2.size())
                        {
                            tiempoTransferencia = filaAP2.get(i) * 0.25;
                        }
                        i++;
                    }while(i <= filaAP2.size() && tiempoTransferencia > tiempo);
                    
                    if(tiempoTransferencia <= tiempo)
                    {
                        vaEnviar = true;
                        filaA -= 1;
                        filaAP2.remove(i-1);
                        /* A termina poner en línea */
                        eventos[6] = reloj + tiempoTransferencia;
                    }
                    else
                    {
                        if(!vaEnviar)
                        {
                            /* B recibe token */
                            eventos[4] = reloj;
                        }
                    }
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
        tieneToken = 2; // B tiene token.
	/*
	Cada vez que una máquina recibe el token guarda el contador de archivos enviados
	anteriormente en un vector y lo reinicia, para empezar a contar cuántos enviará.
	Y así poder sacar el promedio de archivos enviados cada vez que se tiene el token.	
	*/
        cantPorToken.add(contadorPorToken);
        contadorPorToken = 0;
        tiempo = tiempoToken;
        tiempoTransferencia = 0;
        int i = 0;
        boolean vaEnviar = false;
        
        if(filaB != 0)
        {
            if(filaBP1.size() > 0)
            {
		/** Se calcula el tiempo de transferencia de un archivo en la cola
		* si este se puede enviar en el tiempo de token que se tiene, se escoge
		* y sale del ciclo, si no, se continua con el siguiente.
                * Solo se puede salir del ciclo si un archivo se puede enviar, o sea,
                * su tamaño lo permite en el tiempo disponible.
                * O salir del ciclo porque 
		*/
                do
                {
                    if(i < filaBP1.size())
                    {
                        tiempoTransferencia = filaBP1.get(i) * 0.25;
                    }
                    i++;
                }while(i <= filaBP1.size() && tiempoTransferencia > tiempo);
                
		// Si da tiempo de enviar ese archivo, se saca dse la cola y se envía.
                if(tiempoTransferencia <= tiempo)
                {
                    vaEnviar = true;
                    filaB -= 1;
                    filaBP1.remove(i-1);
                    /* B termina poner en línea */
                    eventos[7] = reloj + tiempoTransferencia;
                }
                else
                {
                    if(!vaEnviar)
                    {
                        i = 0;
                        if(filaBP2.size() > 0)
                        {
                            do
                            {   if(i < filaBP2.size())
                                {
                                    tiempoTransferencia = filaBP2.get(i) * 0.25;
                                }
                                i++;
                            }while(i <= filaBP2.size() && tiempoTransferencia > tiempo);

                            if(tiempoTransferencia <= tiempo)
                            {
                                vaEnviar = true;
                                filaB -= 1;
                                filaBP2.remove(i-1);
                                /* B termina poner en línea */
                                eventos[7] = reloj + tiempoTransferencia;
                            }
                            else
                            {
                                if(!vaEnviar)
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
                        if(i < filaBP2.size())
                        {
                            tiempoTransferencia = filaBP2.get(i) * 0.25;
                        }
                        i++;
                    }while(i <= filaBP2.size() && tiempoTransferencia > tiempo);
                    
                    if(tiempoTransferencia <= tiempo)
                    {
                        vaEnviar = true;
                        filaB -= 1;
                        filaBP2.remove(i-1);
                        /* B termina poner en línea */
                        eventos[7] = reloj + tiempoTransferencia;
                    }
                    else
                    {
                        if(!vaEnviar)
                        {
                            /* C recibe token */
                            eventos[5] = reloj;
                        }
                    }
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
        tieneToken = 3; // C tiene token.
	/*
	Cada vez que una máquina recibe el token guarda el contador de archivos enviados
	anteriormente en un vector y lo reinicia, para empezar a contar cuántos enviará.
	Y así poder sacar el promedio de archivos enviados cada vez que se tiene el token.	
	*/
        cantPorToken.add(contadorPorToken);
        contadorPorToken = 0;
        tiempo = tiempoToken;
        tiempoTransferencia = 0;
        int i = 0;
        boolean vaEnviar = false;
        
        if(filaC != 0)
        {
            if(filaCP1.size() > 0)
            {
		/** Se calcula el tiempo de transferencia de un archivo en la cola
		* si este se puede enviar en el tiempo de token que se tiene, se escoge
		* y sale del ciclo, si no, se continua con el siguiente.
                * Solo se puede salir del ciclo si un archivo se puede enviar, o sea,
                * su tamaño lo permite en el tiempo disponible.
                * O salir del ciclo porque 
		*/
                do
                {
                    if(i < filaCP1.size())
                    {
                        tiempoTransferencia = filaCP1.get(i) * 0.25;
                    }
                    i++;
                }while(i <= filaCP1.size() && tiempoTransferencia > tiempo);
                
		// Si da tiempo de enviar ese archivo, se saca dse la cola y se envía.
                if(tiempoTransferencia <= tiempo)
                {
                    vaEnviar = true;
                    filaC -= 1;
                    filaCP1.remove(i-1);
                    /* C termina poner en línea */
                    eventos[8] = reloj + tiempoTransferencia;
                }
                else
                {
                    if(!vaEnviar)
                    {
                        i = 0;
                        if(filaCP2.size() > 0)
                        {
                            do
                            {   if(i < filaCP2.size())
                                {
                                    tiempoTransferencia = filaCP2.get(i) * 0.25;
                                }
                                i++;
                            }while(i <= filaCP2.size() && tiempoTransferencia > tiempo);

                            if(tiempoTransferencia <= tiempo)
                            {
                                vaEnviar = true;
                                filaC -= 1;
                                filaCP2.remove(i-1);
                                /* C termina poner en línea */
                                eventos[8] = reloj + tiempoTransferencia;
                            }
                            else
                            {
                                if(!vaEnviar)
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
                        if(i < filaCP2.size())
                        {
                            tiempoTransferencia = filaCP2.get(i) * 0.25;
                        }
                        i++;
                    }while(i <= filaCP2.size() && tiempoTransferencia > tiempo);
                    
                    if(tiempoTransferencia <= tiempo)
                    {
                        vaEnviar = true;
                        filaC -= 1;
                        filaCP2.remove(i-1);
                        /* C termina poner en línea */
                        eventos[8] = reloj + tiempoTransferencia;
                    }
                    else
                    {
                        if(!vaEnviar)
                        {
                            /* A recibe token */
                            eventos[3] = reloj;
                        }
                    }
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
        boolean vaEnviar = false;
        
	/* Si la máquina todavía tiene tiempo de token escoge otro archivo de la cola
	y para determinar si lo puede enviar dado su tamaño.
	Si no tiene más tiempo pasa el token a la siguiente máquina.
	*/
        if(tiempo != 0)
        {
            /* Si le queda tiempo, pero no tiene archivos en cola pasa el token. */
            if(filaA != 0)
            {
                if(filaAP1.size() > 0)
                {
                    /** Se calcula el tiempo de transferencia de un archivo en la cola
                    * si este se puede enviar en el tiempo de token que se tiene, se escoge
                    * y sale del ciclo, si no, se continua con el siguiente.
                    * Solo se puede salir del ciclo si un archivo se puede enviar, o sea,
                    * su tamaño lo permite en el tiempo disponible.
                    * O salir del ciclo porque 
                    */
                    do
                    {
                        if(i < filaAP1.size())
                        {
                            tiempoTransferencia = filaAP1.get(i) * 0.25;
                        }
                        i++;
                    }while(i <= filaAP1.size() && tiempoTransferencia > tiempo);

                    // Si da tiempo de enviar ese archivo, se saca dse la cola y se envía.
                    if(tiempoTransferencia <= tiempo)
                    {
                        vaEnviar = true;
                        filaA -= 1;
                        filaAP1.remove(i-1);
                        /* A termina poner en línea */
                        eventos[6] = reloj + tiempoTransferencia;
                    }
                    else
                    {
                        if(!vaEnviar)
                        {
                            i = 0;
                            if(filaAP2.size() > 0)
                            {
                                do
                                {   if(i < filaAP2.size())
                                    {
                                        tiempoTransferencia = filaAP2.get(i) * 0.25;
                                    }
                                    i++;
                                }while(i <= filaAP2.size() && tiempoTransferencia > tiempo);

                                if(tiempoTransferencia <= tiempo)
                                {
                                    vaEnviar = true;
                                    filaA -= 1;
                                    filaAP2.remove(i-1);
                                    /* A termina poner en línea */
                                    eventos[6] = reloj + tiempoTransferencia;
                                }
                                else
                                {
                                    if(!vaEnviar)
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
                    }
                }
                else
                {
                    i = 0;
                    if(filaAP2.size() > 0)
                    {
                        do
                        {    
                            if(i < filaAP2.size())
                            {
                                tiempoTransferencia = filaAP2.get(i) * 0.25;
                            }
                            i++;
                        }while(i <= filaAP2.size() && tiempoTransferencia > tiempo);

                        if(tiempoTransferencia <= tiempo)
                        {
                            vaEnviar = true;
                            filaA -= 1;
                            filaAP2.remove(i-1);
                            /* A termina poner en línea */
                            eventos[6] = reloj + tiempoTransferencia;
                        }
                        else
                        {
                            if(!vaEnviar)
                            {
                                /* B recibe token */
                                eventos[4] = reloj;
                                /* A termina poner en línea = infinito; */
                                eventos[6] = Double.MAX_VALUE;
                            }
                        }
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
        boolean vaEnviar = false;
        
	/* Si la máquina todavía tiene tiempo de token escoge otro archivo de la cola
	y para determinar si lo puede enviar dado su tamaño.
	Si no tiene más tiempo pasa el token a la siguiente máquina.
	*/		
        if(tiempo != 0)
        {
            /* Si le queda tiempo, pero no tiene archivos en cola pasa el token. */
            if(filaB != 0)
            {
                if(filaBP1.size() > 0)
                {
                    /** Se calcula el tiempo de transferencia de un archivo en la cola
                    * si este se puede enviar en el tiempo de token que se tiene, se escoge
                    * y sale del ciclo, si no, se continua con el siguiente.
                    * Solo se puede salir del ciclo si un archivo se puede enviar, o sea,
                    * su tamaño lo permite en el tiempo disponible.
                    * O salir del ciclo porque 
                    */
                    do
                    {
                        if(i < filaBP1.size())
                        {
                            tiempoTransferencia = filaBP1.get(i) * 0.25;
                        }
                        i++;
                    }while(i <= filaBP1.size() && tiempoTransferencia > tiempo);

                    // Si da tiempo de enviar ese archivo, se saca dse la cola y se envía.
                    if(tiempoTransferencia <= tiempo)
                    {
                        vaEnviar = true;
                        filaB -= 1;
                        filaBP1.remove(i-1);
                        /* B termina poner en línea */
                        eventos[7] = reloj + tiempoTransferencia;
                    }
                    else
                    {
                        if(!vaEnviar)
                        {
                            i = 0;
                            if(filaBP2.size() > 0)
                            {
                                do
                                {   if(i < filaBP2.size())
                                    {
                                        tiempoTransferencia = filaBP2.get(i) * 0.25;
                                    }
                                    i++;
                                }while(i <= filaBP2.size() && tiempoTransferencia > tiempo);

                                if(tiempoTransferencia <= tiempo)
                                {
                                    vaEnviar = true;
                                    filaB -= 1;
                                    filaBP2.remove(i-1);
                                    /* B termina poner en línea */
                                    eventos[7] = reloj + tiempoTransferencia;
                                }
                                else
                                {
                                    if(!vaEnviar)
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
                    }
                }
                else
                {
                    i = 0;
                    if(filaBP2.size() > 0)
                    {
                        do
                        {    
                            if(i < filaBP2.size())
                            {
                                tiempoTransferencia = filaBP2.get(i) * 0.25;
                            }
                            i++;
                        }while(i <= filaBP2.size() && tiempoTransferencia > tiempo);

                        if(tiempoTransferencia <= tiempo)
                        {
                            vaEnviar = true;
                            filaB -= 1;
                            filaBP2.remove(i-1);
                            /* B termina poner en línea */
                            eventos[7] = reloj + tiempoTransferencia;
                        }
                        else
                        {
                            if(!vaEnviar)
                            {
                                /* C recibe token */
                                eventos[5] = reloj;
                                /* B termina poner en línea = infinito; */
                                eventos[7] = Double.MAX_VALUE;
                            }
                        }
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
        boolean vaEnviar = false;

	/* Si la máquina todavía tiene tiempo de token escoge otro archivo de la cola
	y para determinar si lo puede enviar dado su tamaño.
	Si no tiene más tiempo pasa el token a la siguiente máquina.
	*/        
        if(tiempo != 0)
        {
            /* Si le queda tiempo, pero no tiene archivos en cola pasa el token. */
            if(filaC != 0)
            {
                if(filaCP1.size() > 0)
                {
                    /** Se calcula el tiempo de transferencia de un archivo en la cola
                    * si este se puede enviar en el tiempo de token que se tiene, se escoge
                    * y sale del ciclo, si no, se continua con el siguiente.
                    * Solo se puede salir del ciclo si un archivo se puede enviar, o sea,
                    * su tamaño lo permite en el tiempo disponible.
                    * O salir del ciclo porque 
                    */
                    do
                    {
                        if(i < filaCP1.size())
                        {
                            tiempoTransferencia = filaCP1.get(i) * 0.25;
                        }
                        i++;
                    }while(i <= filaCP1.size() && tiempoTransferencia > tiempo);

                    // Si da tiempo de enviar ese archivo, se saca dse la cola y se envía.
                    if(tiempoTransferencia <= tiempo)
                    {
                        vaEnviar = true;
                        filaC -= 1;
                        filaCP1.remove(i-1);
                        /* C termina poner en línea */
                        eventos[8] = reloj + tiempoTransferencia;
                    }
                    else
                    {
                        if(!vaEnviar)
                        {
                            i = 0;
                            if(filaCP2.size() > 0)
                            {
                                do
                                {   if(i < filaCP2.size())
                                    {
                                        tiempoTransferencia = filaCP2.get(i) * 0.25;
                                    }
                                    i++;
                                }while(i <= filaCP2.size() && tiempoTransferencia > tiempo);

                                if(tiempoTransferencia <= tiempo)
                                {
                                    vaEnviar = true;
                                    filaC -= 1;
                                    filaCP2.remove(i-1);
                                    /* C termina poner en línea */
                                    eventos[8] = reloj + tiempoTransferencia;
                                }
                                else
                                {
                                    if(!vaEnviar)
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
                    }
                }
                else
                {
                    i = 0;
                    if(filaCP2.size() > 0)
                    {
                        do
                        {    
                            if(i < filaCP2.size())
                            {
                                tiempoTransferencia = filaCP2.get(i) * 0.25;
                            }
                            i++;
                        }while(i <= filaCP2.size() && tiempoTransferencia > tiempo);

                        if(tiempoTransferencia <= tiempo)
                        {
                            vaEnviar = true;
                            filaC -= 1;
                            filaCP2.remove(i-1);
                            /* C termina poner en línea */
                            eventos[8] = reloj + tiempoTransferencia;
                        }
                        else
                        {
                            if(!vaEnviar)
                            {
                                /* A recibe token */
                                eventos[3] = reloj;
                                /* C termina poner en línea = infinito; */
                                eventos[8] = Double.MAX_VALUE;
                            }
                        }
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
        int probabilidadEnviar;
	
		/* Si un archivo llega al antivirus y lo encuentra libre procede a hacer la revisión.
		Si no, se agrega a la cola.
		*/
        if(libreAntivirus)
        {
            libreAntivirus = false;
            probabilidadEnviar = tieneVirus(); // Variable aleatoria para decidir si se descarta o se envía.
            /* Se genera la bandera de envío y duración total de revisión */
            if(probabilidadEnviar == 4)
            {
				/*Luego de tres revisiones al archivo se le detectó virus, por lo que no se envía.*/
                enviar = false;
                duracionTotalRevision = ((tamanoArchv/8)+(tamanoArchv/16)+(tamanoArchv/24));
            }
            else
            {
                enviar = true;
				/*Luego de una revisión no se le encontró virus al archivo, por lo que se envía.*/
                if(probabilidadEnviar == 1)
                {
                    duracionTotalRevision = tamanoArchv/8;
                }
				/*Luego de dos revisiones no se le encontró virus al archivo, por lo que se envía.*/
                if(probabilidadEnviar == 2)
                {
                    duracionTotalRevision = ((tamanoArchv/8)+(tamanoArchv/16));
                }
				/*Luego de tres revisiones no se le encontró virus al archivo, por lo que se envía.*/
                if(probabilidadEnviar == 3)
                {
                    duracionTotalRevision = ((tamanoArchv/8)+(tamanoArchv/16)+(tamanoArchv/24));
                }
            }
			// Se pasa el tamaño de dicho archivo para su posterior uso y así no caerle encima.
            tamArchvLibera = tamanoArchv;
            /* Se libera antivirus */
            eventos[10] = reloj + duracionTotalRevision;
        }
        else
        {
            filaAntivirus++;
            colaAntivirus.add(tamanoArchv);
        }        
        /* Llegada antivirus = infinito */
        eventos[9] = Double.MAX_VALUE;
    }
    
    public void liberaAntivirus(double horaEvento)
    {
        reloj = horaEvento;
        int probabilidadEnviar;
        int tamArchvLibera2;
        
		/* Si se determina que el archivo no contenía virus luego de las revisiones
		se puede enviar mediante el router.
		*/
        if(enviar)
        {
			// Cuento cuántos archivos fueron enviados, es decir, aprobaron las revisiones.
            archivosEnviados++;
			// Si ambas líneas están desocupadas se escoge cualquiera.
            if(linea1 && linea2)
            {
                int linea = escogerLinea(); // Escoge cuál línea agarra
                if(linea == 1)
                {
                    enviadosL1++;
                    linea1 = false;
                    duracionTransmisionL1 = tamArchvLibera / 64;
                    tamArchvL1 = tamArchvLibera;
                    /* Se libera línea 1 */
                    eventos[11] = reloj + duracionTransmisionL1;
                }
                else
                {
                    enviadosL2++;
                    linea2 = false;
                    duracionTransmisionL2 = tamArchvLibera / 64;
                    tamArchvL2 = tamArchvLibera;
                    /* Se libera línea 2 */
                    eventos[12] = reloj + duracionTransmisionL2;
                }
            }
			// Si solo una está desocupada, se revisa cuál es.
            else
            {
                if(linea1)
                {
                    enviadosL1++;
                    linea1 = false;
                    duracionTransmisionL1 = tamArchvLibera / 64;
                    tamArchvL1 = tamArchvLibera;
                    /* Se libera línea 1 */
                    eventos[11] = reloj + duracionTransmisionL1;
                }
                else
                {
                    if(linea2)
                    {
                        enviadosL2++;
                        linea2 = false;
                        duracionTransmisionL2 = tamArchvLibera / 64;
                        tamArchvL2 = tamArchvLibera;
                        /* Se libera línea 2 */
                        eventos[12] = reloj + duracionTransmisionL2;
                    }
					// Si ninguna línea estaba desocupada se aumenta la "cola del router".
                    else
                    {
                        filaRouter++;
                        colaRouter.add(tamArchvLibera);
                    }
                }
            }
        }
        else
        {
			// Se cuenta cuántos archivos no fueron enviados, es decir, no pasaron la prueba.
            archivosNoEnviados++;
        }
		/*
		Una vez que se envia o no el archivo, se procede a tomar el siguiente archivo en cola.
		Si no hay, se libera el antivirus.
		*/
        if(filaAntivirus != 0)
        {
            libreAntivirus = false;
			// Se toma el tamaño del siguiente archivo para calcular su tiempo de revisión.
            tamArchvLibera2 = colaAntivirus.get(0);
			// Se reduce la fila del antivirus.
            colaAntivirus.remove(0);
            filaAntivirus--;
            probabilidadEnviar = tieneVirus(); // Variable aleatoria para decidir si se descarta o se envía.
            /* Se genera la bandera de envío y duración total de revisión */
            if(probabilidadEnviar == 4)
            {
				/*Luego de tres revisiones al archivo se le detectó virus, por lo que no se envía.*/
                enviar = false;
                duracionTotalRevision = ((tamArchvLibera2/8)+(tamArchvLibera2/16)+(tamArchvLibera2/24));
            }
            else
            {
                enviar = true;
				/*Luego de una revisión no se le encontró virus al archivo, por lo que se envía.*/
                if(probabilidadEnviar == 1)
                {
                    duracionTotalRevision = tamArchvLibera2/8;
                }
				/*Luego de dos revisiones no se le encontró virus al archivo, por lo que se envía.*/
                if(probabilidadEnviar == 2)
                {
                    duracionTotalRevision = ((tamArchvLibera2/8)+(tamArchvLibera2/16));

                }
				/*Luego de tres revisiones no se le encontró virus al archivo, por lo que se envía.*/
                if(probabilidadEnviar == 3)
                {
                    duracionTotalRevision = ((tamArchvLibera2/8)+(tamArchvLibera2/16)+(tamArchvLibera2/24));
                }
            }
            /* Se libera antivirus */
            eventos[10] = reloj + duracionTotalRevision;
        }
        else
        {
            libreAntivirus = true;
            /* Libera antivirus = infinito */
            eventos[10] = Double.MAX_VALUE;
        }        
    }
    
    public void liberaLinea1(double horaEvento)
    {
        reloj = horaEvento;
        
		/*Si al liberarse la línea hay más archivos en cola, toma el siguiente.*/
        if(filaRouter != 0)
        {
            enviadosL1++;
            linea1 = false;
			// Se toma el tamaño del archivo para calcular su tiempo de transmisión.
            tamArchvL1 = colaRouter.get(0);
            filaRouter--;
            colaRouter.remove(0);
            duracionTransmisionL1 = tamArchvL1 / 64;
            /* Se libera línea 1 */
            eventos[11] = reloj + duracionTransmisionL1;
        }
		/*Si no, se libera*/
        else
        {
            linea1 = true;
            /* Se libera línea 1 = infinito */
            eventos[11] = Double.MAX_VALUE;
        }
    }
    public void liberaLinea2(double horaEvento)
    {
        reloj = horaEvento;
        
		/*Si al liberarse la línea hay más archivos en cola, toma el siguiente.*/
        if(filaRouter != 0)
        {
            enviadosL2++;
            linea2 = false;
			// Se toma el tamaño del archivo para calcular su tiempo de transmisión.
            tamArchvL2 = colaRouter.get(0);
            filaRouter--;
            colaRouter.remove(0);
            duracionTransmisionL2 = tamArchvL2 / 64;
            /* Se libera línea 2 */
            eventos[12] = reloj + duracionTransmisionL2;
        }
		/*Si no, se libera*/
        else
        {
            linea2 = true;
            /* Se libera línea 2 = infinito */
            eventos[12] = Double.MAX_VALUE;
        }
    }

    /************* GENERACIÓN DE NÚMEROS ALEATORIOS ***************************/
    
    /**
     * Asignar la cantidad de revisiones y  si tiene virus o no
     */  
    public int tieneVirus()
    {
        int i = m_random.nextInt(1000000); // Valor entre 0 (incluido) y 1000000 (excluido).
        int j = 0;
        if(i < 950000)
        {
            j = 1;
            cantRevArchv.add(1); // Se hace una revisión.
        }
        if(i >= 950000 && i < 997500)
        {
            j = 2;
            cantRevArchv.add(2); // Se hacen dos revisiones.
        }
        if(i >= 997500 && i < 999875)
        {
            j = 3;
            cantRevArchv.add(3); // Se hacen tres revisiones.
        }
        if(i >= 999875 && i < 1000000)
        {
            j = 4;
            cantRevArchv.add(3); // Se hacen tres revisiones.
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
     * Escoge cuál línea del router usar
     * Variable aleatoria uniforme: X={1,2}
     */
    public int escogerLinea()
    {
        int numLinea = 0;
        int i = m_random.nextInt(10); // Valor entre 0 (incluido) y 10 (excluido).
        if(i < 5)   // 0 <= i < 5
        {
            numLinea = 1;
        }
        else        // 5 <= i < 10
        {
            numLinea = 2;
        }
        return numLinea;
    }
    
    /**
     * Asigna tamaño a un archivo.
     * Variable aleatoria uniforme: X={1,2,3,...,64}
     */
    public int asignarTamano()
    {
        int tamano= 0;
        int i = m_random.nextInt(1000000); // Valor entre 0 (incluido) y 1000000 (excluido).
        if(i < 15625){ tamano = 1; }
        if( i >= 15625 && i < 31250){ tamano = 2; }
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
     * Numeros aleatorios con distribucion exponencial con media de 1/25 segundos
     */
    public double proximoArriboA()
    {
        double lambda = 0.04;
        double x, r;
        r = m_random.nextDouble();
        x = ((-Math.log(1-r))/lambda);
        return x;
    }

    /**
     * Numeros aleatorios con distribucion f(x)=x/40 8 <= x <= 12
     */
    public double proximoArriboB()
    {
        double r = m_random.nextDouble();
        double proximo = Math.sqrt(40*r+64);
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
    
    /************************ CÁLCULO DE ESTADÍSTICAS *************************/
    
    public void calcularEstadisticas(int numeroCorrida)
    {
        promedioEnviadosToken = promedioEnviadosPorToken();
        promedioRevisiones = promedioRevisionesPorArchivo();
        promedioColas(numeroCorrida);
    }
    
    public double promedioEnviadosPorToken()
    {
        double total = 0;
        for(int i = 0; i < cantPorToken.size(); i++)
        {
            total += (double) cantPorToken.get(i);
        }
        return total/((double)cantPorToken.size());
    }
    
    public double promedioRevisionesPorArchivo()
    {
        double total = 0;
        for(int i = 0; i < cantRevArchv.size(); i++)
        {
            total += (double) cantRevArchv.get(i);
        }
        return total/((double)cantRevArchv.size());
    }
    
    /**
     *      NCorrida 1 2 ... n
     * ColaA        | | |   | |
     * ColaB        | | |   | |
     * ColaC        | | |   | |
     * ColaAnt      | | |   | |
     * @param numeroCorrida 
     */
    public void promedioColas(int numeroCorrida)
    {
        colasFinales[0][numeroCorrida] = filaA ;
        colasFinales[1][numeroCorrida] = filaB ;
        colasFinales[2][numeroCorrida] = filaC ;
        colasFinales[3][numeroCorrida] = filaAntivirus ;
    }
    
    /**
     * Saca el promedio de la matriz de colasFinales de todas las corridas
     * 
     */
    public void promedioColasFinal(int numeroCorridas)
    {
        double suma = 0;
        for(int i=0 ; i<4 ; ++i)
        {
            for(int j =0 ; j<numeroCorridas ;++j)
            {
                suma += colasFinales[i][j];
            }
            promedioColas[i] = suma/numeroCorridas;
            suma = 0;
        }
        /*System.out.println("Cola de A final");
        for(int i =0; i <numeroCorridas;++i){
            System.out.print(colasFinales[0][i]+" , ");
        }
        System.out.println();
            System.out.println("Cola de B final");
        for(int i =0; i <numeroCorridas;++i){
            System.out.print(colasFinales[1][i]+" , ");
        }
        System.out.println();
        System.out.println("Cola de C final");
        for(int i =0; i <numeroCorridas;++i){
            System.out.print(colasFinales[2][i]+" , ");
        }
        System.out.println();
        System.out.println("Cola de Antivirus final");
        for(int i =0; i <numeroCorridas;++i){
            System.out.print(colasFinales[3][i]+" , ");
        }*/        
    }
    
    public void resetearVaraibles()
    {
        
    }
}
