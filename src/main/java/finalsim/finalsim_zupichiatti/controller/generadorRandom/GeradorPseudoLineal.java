package finalsim.finalsim_zupichiatti.controller.generadorRandom;

import finalsim.finalsim_zupichiatti.controller.utils.ConstantesGenerador;
import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;

import org.springframework.stereotype.Component;


@Component(ConstantesGenerador.LINEAL)
public class GeradorPseudoLineal implements IGeneradorRandom{
    @Override
    public Pseudoaleatorio[] generar(ParametrosGenerador parametros) {

        if(parametros == null) throw new IllegalArgumentException("No es posible generar un conjunto de números aleatorios sin parámetros");
        if(parametros.getN() <= 0 || parametros.getN() > 1000) parametros.setN(30);
        if(parametros.getPresicion() <= 0 || parametros.getPresicion() > 9) parametros.setPresicion(4);
        if(parametros.getX0() < 0) throw new IllegalArgumentException("No es posible el ingreso de una semilla negativa");
        if(parametros.getG() <= 0) throw new IllegalArgumentException("G debe ser mayor a 0");
        if(parametros.getK() <= 0) throw new IllegalArgumentException("K debe ser mayor a 0");
        if(parametros.getC() <= 0) throw new IllegalArgumentException("C debe ser mayor a 0");
        int m = (int)Math.pow(2, parametros.getG());
        if(!primosRelativos(m, parametros.getC()))throw new IllegalArgumentException("M y C deben ser primos relativos, con M=2^G");
        int a = 1 + (4* parametros.getK());
        Pseudoaleatorio[] resultados = new Pseudoaleatorio[parametros.getN()];
        int xi = parametros.getX0(); //Cambio de variable solo por comodidad;

        int multiplicador = (int)Math.pow(10, parametros.getPresicion());
        for(int i = 0; i< parametros.getN(); i++){
            Pseudoaleatorio pseudoaleatorio = new Pseudoaleatorio();
            xi = (a*xi)+ parametros.getC();
            pseudoaleatorio.setAxi(xi);
            xi = xi % m;
            pseudoaleatorio.setSemilla(xi);
            float random = ((float)xi/(m));
            int randAux = (int)(random * multiplicador);
            random = (float)randAux/multiplicador;
            pseudoaleatorio.setRandom(random);
            pseudoaleatorio.setI(i+1);
            resultados[i] = pseudoaleatorio;
        }
        return resultados;
    }

    @Override
    public Pseudoaleatorio siguientePseudoAleatoreo(Pseudoaleatorio pseudoaleatorio, ParametrosGenerador parametros) {
        if(pseudoaleatorio == null || parametros == null) throw new IllegalArgumentException("Se deben proporcionar parametros válidos");
        if(pseudoaleatorio.getSemilla() < 0) pseudoaleatorio.setSemilla(0);
        if(parametros.getPresicion() <= 0 || parametros.getPresicion() > 9) parametros.setPresicion(4);
        if(parametros.getG() <= 0) throw new IllegalArgumentException("G debe ser mayor a 0");
        if(parametros.getK() <= 0) throw new IllegalArgumentException("K debe ser mayor a 0");

        int m = (int)Math.pow(2, parametros.getG());
        int multiplicador = (int)Math.pow(10, parametros.getPresicion());
        if(!primosRelativos(m, parametros.getC()))throw new IllegalArgumentException("M y C deben ser primos relativos, con M=2^G");
        int a = 1 + (4* parametros.getK());
        Pseudoaleatorio resultado = new Pseudoaleatorio();
        int xi = pseudoaleatorio.getSemilla();
        xi = ((a*xi)+ parametros.getC());
        resultado.setAxi(xi);
        xi = xi %m;
        resultado.setSemilla(xi);
        float random = (float)xi/(m);
        int auxRand = (int)(random*multiplicador);
        random = (float)auxRand/multiplicador;
        resultado.setRandom(random);
        return resultado;
    }

    private boolean primosRelativos(int m, int c){
        //Dos numeros son primos relativos si no tienen otro divisor
        //común más que 1 y -1 por tanto lo que se debe hacer es calcular
        //el maximo divisor común entre dos numeros dados, y luego si el
        //resultado es igual a 1 o a -1, entonces los numeros son primos relativos, de lo contrario, no lo son.

        //calcular el maximo divisor común
        int resto;
        while (c != 0){
            resto = m % c;
            m = c;
            c = resto;
        }
        if (m == 1 || m == -1){
            return true;
        }
        return false;
    }

    @Override
    public Pseudoaleatorio siguientePseudoAleatoreo(ParametrosGenerador parametrosGenerador) {
        if(parametrosGenerador == null)
            throw new IllegalArgumentException("Es necesario brindar los parametros para generar el número");
        parametrosGenerador.setN(1);
        return generar(parametrosGenerador)[0];
    }
}
