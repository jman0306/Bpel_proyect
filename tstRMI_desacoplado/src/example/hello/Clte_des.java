/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//================================================================
// Código del Cliente:
//================================================================
package example.hello;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;




public class Clte_des 
{
    //private Cliente() {}
    public static void main(String[] args) 
    {
       interfazservicioestres.InterfazServiciosEstres objServ;
       long lngQuienSoy;
       long sumDeltaT, sumDeltaT2, dtMin = 0,dtMax = 0;
       long lngCuantosMilisFaltan;
       
       String host = (args.length < 1) ? null : args[0];
       long i,n,t0,t1,dt;
       String response;
       
       n = 25;
       
       
       try 
        {
             objServ = (interfazservicioestres.InterfazServiciosEstres)
                     Class.forName("pojo_carrent.POJO_CarRent").newInstance();
            
             Registry registry = LocateRegistry.getRegistry(host);
             IServDisparo servDisparo = (IServDisparo) registry.lookup("ServidorDeDisparo");
             lngQuienSoy = servDisparo.quienSoy();
             
             objServ.inizializa((int) lngQuienSoy);
             
             
             lngCuantosMilisFaltan = servDisparo.deltaTEnMilis();
             System.out.println("Cliente " + lngQuienSoy + " faltan " + lngCuantosMilisFaltan  + " milisegundos");
             sumDeltaT  = 0;
             sumDeltaT2 = 0;
             Hello stub = (Hello) registry.lookup("Hello");
             Thread.currentThread().sleep(lngCuantosMilisFaltan);
             
             for(i= 0; i < n; i++ )
             {
               t0 = System.currentTimeMillis(); 
               
               objServ.invocaServicio((int)i);
                       
               t1 = System.currentTimeMillis();
               dt = t1 - t0;
               sumDeltaT  += dt;
               sumDeltaT2 += dt * dt;
               if( i == 0 )
               {
                   dtMin = dt;
                   dtMax = dt;
               }
               else
               {
                   if( dt < dtMin ) dtMin = dt;
                   if( dt > dtMax ) dtMax = dt;
               }
//               System.out.println("Clte " + lngQuienSoy + ": " + response);
             }
             servDisparo.acumula(sumDeltaT, sumDeltaT2, n, dtMax, dtMin);
             objServ.cierra();
          } 
          catch (Exception e)
          {
              System.err.println("Client exception: " + e.toString());
               e.printStackTrace();
           }
    }
}
//================================================================

