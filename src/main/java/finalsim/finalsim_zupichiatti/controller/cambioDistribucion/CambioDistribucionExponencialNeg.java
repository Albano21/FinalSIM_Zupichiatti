package finalsim.finalsim_zupichiatti.controller.cambioDistribucion;


import k1.grupo.p.simulacionestp5colas.controller.generadorRandom.IGeneradorRandom;
import k1.grupo.p.simulacionestp5colas.controller.utils.ConstantesCambioDistribucion;
import k1.grupo.p.simulacionestp5colas.modelo.ParametrosCambioDistribucion;
import k1.grupo.p.simulacionestp5colas.modelo.ParametrosGenerador;
import k1.grupo.p.simulacionestp5colas.modelo.Pseudoaleatorio;
import k1.grupo.p.simulacionestp5colas.modelo.VaribaleAleatoria;
import k1.grupo.p.simulacionestp5colas.modelo.bondadAjuste.Intervalo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component(ConstantesCambioDistribucion.EXP_NEG)
public class CambioDistribucionExponencialNeg implements ICambioDistribucion{

    @Autowired
    private Map<String, IGeneradorRandom> generadoresRandom;

    @Override
    public Pseudoaleatorio[] cambiarDistribucion(ParametrosCambioDistribucion parametros, ParametrosGenerador... parametrosGenerador) {

        //
        if(parametros.getPresicion() <= 0 || parametros.getPresicion() > 9) parametros.setPresicion(4);

        ParametrosGenerador parametrosGenerador1 = Arrays.stream(parametrosGenerador).iterator().next();
        IGeneradorRandom generadorRandom = generadoresRandom.get(parametrosGenerador1.getMetodoGeneradorRandom());

        //defino vector de randoms exponenciales negativos
        int n = parametros.getN();
        Pseudoaleatorio pseudoaleatoriosExpNeg[] = new Pseudoaleatorio[n];

        //defino vector de randoms uninformes y genero randoms uninformes
        parametrosGenerador1.setN(n);
        Pseudoaleatorio randomsUnif[] = generadorRandom.generar(parametrosGenerador1);
        Pseudoaleatorio randomUnif;

        // genero randoms exponenciales negativos
        float pseudoAleatorioExpNeg;
        int multiplicador = (int)Math.pow(10, parametros.getPresicion());
        int aux;
        for(int i = 0; i < n; i++){
            randomUnif = randomsUnif[i];
            pseudoAleatorioExpNeg = (float) ((-1/parametros.getLambda())*Math.log(1-randomUnif.getRandom()));
            aux = (int) (pseudoAleatorioExpNeg*multiplicador);
            pseudoAleatorioExpNeg = (float)aux/multiplicador;
            pseudoaleatoriosExpNeg[i] = new Pseudoaleatorio(i,pseudoAleatorioExpNeg);
        }

        return pseudoaleatoriosExpNeg;
    }
    @Override
    public Intervalo[] generarDistFrecuenciaInicial(Pseudoaleatorio[] randoms,
                                                    ParametrosCambioDistribucion parametrosCambioDistribucion){

        float[]minYMax = buscarMinYMax(randoms);
        Intervalo[] distFrecInicial = inicializarDistFrecuencia(minYMax,
                parametrosCambioDistribucion.getKInicial(),parametrosCambioDistribucion.getPresicion());
        int multiplicador = (int)Math.pow(10, parametrosCambioDistribucion.getPresicion());
        float limInf, limSup;
        for(int i = 0; i < distFrecInicial.length; i++){
            limInf = distFrecInicial[i].getLimInf();
            limSup = distFrecInicial[i].getLimSup();
            distFrecInicial[i]
                    .setProbEsp(calcularProbEsperada(parametrosCambioDistribucion,limInf,limSup));
            float frecEsp = distFrecInicial[i].getProbEsp()*randoms.length;
            int frecEspAux = (int)(frecEsp*multiplicador);
            distFrecInicial[i].setFrecEsp((float)frecEspAux/multiplicador);
        }
        llenarDistribucionFrecuencia(randoms,distFrecInicial, parametrosCambioDistribucion.getPresicion());
        return distFrecInicial;
    }
    private void llenarDistribucionFrecuencia(Pseudoaleatorio[] randoms, Intervalo[] distFrecuencia,int presicion){

        for(Pseudoaleatorio random : randoms){

            for(int i = 0 ; i < distFrecuencia.length;i++){
                if((random.getRandom() >= distFrecuencia[i].getLimInf()
                        && random.getRandom() < distFrecuencia[i].getLimSup())||
                        ((i == (distFrecuencia.length-1))&&
                                random.getRandom()==distFrecuencia[i].getLimSup())){
                    distFrecuencia[i].incrementarFrecObservada();
                }
            }

        }
        int multiplicador = (int)Math.pow(10,presicion);
        for(Intervalo intervalo: distFrecuencia){
            float probObs = ((float)intervalo.getFrecObs()/ randoms.length);
            probObs = ((int)(probObs*multiplicador))/(float)multiplicador;
            intervalo.setProbObs(probObs);
        }

    }
    private Intervalo[] inicializarDistFrecuencia(float[]minYMax, int k, int presicion ){

        int multiplicador = (int)Math.pow(10,presicion);
        float amplitudIntervalo = (minYMax[1]-minYMax[0])/k;
        int limInfAux = (int)(minYMax[0]*multiplicador);
        float limInf = (float)limInfAux/multiplicador;
        float limSup;
        Intervalo [] distFrecuencia = new Intervalo[k];
        for(int i = 0; i < distFrecuencia.length; i++){
            Intervalo intervalo = new Intervalo();
            intervalo.setLimInf(limInf);
            limSup = limInf + amplitudIntervalo;
            limSup = ((int)(limSup*multiplicador)/(float)multiplicador);
            intervalo.setLimSup(limSup);
            float marcaClase = ((int)(((limSup+limInf)/2)*multiplicador))/(float)multiplicador;
            intervalo.setMarcaClase(marcaClase);
            distFrecuencia[i] = intervalo;

            limInf = limSup;
            limInf = ((int)(limInf*multiplicador)/(float)multiplicador);
        }
        return distFrecuencia;
    }

    private float[] buscarMinYMax(Pseudoaleatorio[] randoms){
        float max = 0;
        float min = randoms[0].getRandom();

        for(int i = 0; i< randoms.length; i++){
            max = Math.max(randoms[i].getRandom(), max);
            min = Math.min(randoms[i].getRandom(),min);
        }
        return new float[]{min, max};
    }

    private float calcularProbEsperada(ParametrosCambioDistribucion parametrosCambioDistribucion, float limInf, float limSup) {

        float probAcumSup = 1 - (float)Math.exp((-parametrosCambioDistribucion.getLambda())*limSup);
        float probAcumInf = 1 - (float)Math.exp((-parametrosCambioDistribucion.getLambda())*limInf);
        float probEsp = probAcumSup-probAcumInf;
        int multiplicador = (int)Math.pow(10,parametrosCambioDistribucion.getPresicion());
        int probAux = (int)(probEsp*multiplicador);
        return (float)probAux/multiplicador;
    }

    @Override
    public VaribaleAleatoria siguienteRandom(ParametrosCambioDistribucion parametrosCambioDistribucion,
                                             ParametrosGenerador parametrosGenerador,
                                             Pseudoaleatorio randomCUBase) {

        if(parametrosCambioDistribucion == null || parametrosCambioDistribucion.getLambda() <= 0){
            throw new IllegalArgumentException("Debe indicar un valor positivo para lambda");
        }
        if(randomCUBase == null || randomCUBase.getRandom() < 0 || randomCUBase.getRandom() > 1){
            throw new IllegalArgumentException("Debe indicar random uniforme 0-1 válido");
        }
        if(parametrosGenerador == null || parametrosGenerador.getMetodoGeneradorRandom() == null)
            throw new IllegalArgumentException("Debe indicar el método con el que desea general los randoms uniformes 0-1");
        IGeneradorRandom generadorRandom = generadoresRandom
                .get(parametrosGenerador.getMetodoGeneradorRandom());
        int multiplicador = (int)Math.pow(10, parametrosCambioDistribucion.getPresicion());

        float random = (-1)/parametrosCambioDistribucion.getLambda()*(float)Math.log(1-randomCUBase.getRandom());
        int randomAux = (int)(random*multiplicador);
        random = (float)randomAux/multiplicador;
        randomCUBase = generadorRandom.siguientePseudoAleatoreo(randomCUBase,parametrosGenerador);
        return new VaribaleAleatoria(random,randomCUBase);
    }
}
