package finalsim.finalsim_zupichiatti.controller.cambioDistribucion;

import finalsim.finalsim_zupichiatti.controller.utils.ConstantesCambioDistribucion;
import finalsim.finalsim_zupichiatti.controller.generadorRandom.IGeneradorRandom;
import finalsim.finalsim_zupichiatti.modelo.ParametrosCambioDistribucion;
import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;
import finalsim.finalsim_zupichiatti.modelo.VaribaleAleatoria;
import finalsim.finalsim_zupichiatti.modelo.bondadAjuste.Intervalo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component(ConstantesCambioDistribucion.UNIFORME)
public class CambioDistribucionUniformeAB implements ICambioDistribucion{

    @Autowired
    private Map<String, IGeneradorRandom> generadoresRandom;
    @Override
    public Pseudoaleatorio[] cambiarDistribucion(ParametrosCambioDistribucion parametros,
                                                 ParametrosGenerador... parametrosGeneradorList) {
        if(parametros.getUnifA() <= 0 || parametros.getUnifB() <= 0)
            throw new IllegalArgumentException("Debe indiar los limites inferior y superior");
        ParametrosGenerador parametrosGenerador = Arrays.stream(parametrosGeneradorList).iterator().next();
        IGeneradorRandom generadorRandom = generadoresRandom.get(parametrosGenerador.getMetodoGeneradorRandom());
        parametrosGenerador.setN(1);
        Pseudoaleatorio randomUnif = generadorRandom.generar(parametrosGenerador)[0];
        Pseudoaleatorio[] pseudosRandomAB = new Pseudoaleatorio[parametros.getN()];
        int multiplicador = (int)Math.pow(10, parametros.getPresicion());
        int aux;
        for(int i = 0; i < parametros.getN(); i++){
            float random = parametros.getUnifA()
                    + (randomUnif.getRandom()*(parametros.getUnifB()-parametros.getUnifA()));
            aux = (int)(random*multiplicador);
            random = (float)aux/multiplicador;
            pseudosRandomAB[i] = new Pseudoaleatorio(i+1, random);
            randomUnif = generadorRandom.siguientePseudoAleatoreo(randomUnif,parametrosGenerador);
        }

        return pseudosRandomAB;
    }
    @Override
    public Intervalo[] generarDistFrecuenciaInicial(Pseudoaleatorio[] randoms,
                                                    ParametrosCambioDistribucion parametrosCambioDistribucion){

        float[]minYMax = buscarMinYMax(randoms);
        Intervalo[] distFrecInicial = inicializarDistFrecuencia(minYMax,
                parametrosCambioDistribucion.getKInicial(),parametrosCambioDistribucion.getPresicion());
        int multiplicador = (int)Math.pow(10, parametrosCambioDistribucion.getPresicion());
        float probEsp = (float)distFrecInicial.length/randoms.length;
        int probEspAux = (int)(probEsp*multiplicador);
        probEsp = (float)probEspAux/multiplicador;
        for(int i = 0; i < distFrecInicial.length; i++){

            distFrecInicial[i].setProbEsp(probEsp);
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

    @Override
    public VaribaleAleatoria siguienteRandom(ParametrosCambioDistribucion parametros,
                                             ParametrosGenerador parametrosGenerador,
                                             Pseudoaleatorio randomUnifBase) {

        if(parametros == null || parametros.getUnifA() < 0 || parametros.getUnifB() <= 0)
            throw new IllegalArgumentException("Debe indiar los limites inferior y superior");
        if(parametrosGenerador == null || parametrosGenerador.getMetodoGeneradorRandom() == null)
            throw new IllegalArgumentException("Debe indicar el método con el que desea general los randoms uniformes 0-1");
        IGeneradorRandom generadorRandom = generadoresRandom
                .get(parametrosGenerador.getMetodoGeneradorRandom());
        int multiplicador = (int)Math.pow(10, parametros.getPresicion());

        float random = parametros.getUnifA()
                + (randomUnifBase.getRandom()*(parametros.getUnifB()-parametros.getUnifA()));
        int aux = (int)(random*multiplicador);
        randomUnifBase = generadorRandom
                .siguientePseudoAleatoreo(randomUnifBase, parametrosGenerador);
        return new VaribaleAleatoria((float)aux/multiplicador,randomUnifBase);
    }
}
