package finalsim.finalsim_zupichiatti.controller.generadorRandom;


import finalsim.finalsim_zupichiatti.controller.utils.ConstantesGenerador;
import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;
import org.springframework.stereotype.Component;


@Component(ConstantesGenerador.MULTIPLICATIVO)
public class GeneradorPseudoMultiplicativo implements IGeneradorRandom{
    @Override
    public Pseudoaleatorio[] generar(ParametrosGenerador parametros) {
        if(parametros == null) throw new IllegalArgumentException("No es posible generar un conjunto de números aleatorios sin parámetros");
        if(parametros.getN() <= 0 || parametros.getN() > 1000) parametros.setN(30);
        if(parametros.getPresicion() <= 0 || parametros.getPresicion() > 9) parametros.setPresicion(4);
        if(parametros.getX0() < 0) throw new IllegalArgumentException("No es posible el ingreso de una semilla negativa");
        if(parametros.getG() <= 0) throw new IllegalArgumentException("G debe ser mayor a 0");
        if(parametros.getK() <= 0) throw new IllegalArgumentException("K debe ser mayor a 0");
        if(parametros.getX0()%2== 0)throw new IllegalArgumentException("X0 debe ser impar");
        int m = (int)Math.pow(2, parametros.getG());

        int a = 3 + (8* parametros.getK());
        Pseudoaleatorio[] resultados = new Pseudoaleatorio[parametros.getN()];
        int xi = parametros.getX0(); //Cambio de variable solo por comodidad;

        int multiplicador = (int)Math.pow(10, parametros.getPresicion());
        for(int i = 0; i< parametros.getN(); i++){
            Pseudoaleatorio pseudoaleatorio = new Pseudoaleatorio();
            xi = (a*xi);
            pseudoaleatorio.setAxi(xi);
            xi = xi % m;
            pseudoaleatorio.setSemilla(xi);
            float random = ((float)xi/m);
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
        if(pseudoaleatorio.getSemilla()%2== 0)throw new IllegalArgumentException("aXi debe ser impar");
        int m = (int)Math.pow(2, parametros.getG());
        int multiplicador = (int)Math.pow(10, parametros.getPresicion());
        int a = 3 + (8* parametros.getK());
        Pseudoaleatorio resultado = new Pseudoaleatorio();
        int xi = pseudoaleatorio.getSemilla();
        xi = (a*xi);
        resultado.setAxi(xi);
        xi = xi %m;
        resultado.setSemilla(xi);
        float random = (float)xi/m;
        int auxRand = (int)(random*multiplicador);
        random = (float)auxRand/multiplicador;
        resultado.setRandom(random);
        return resultado;
    }
    @Override
    public Pseudoaleatorio siguientePseudoAleatoreo(ParametrosGenerador parametrosGenerador) {
        if(parametrosGenerador == null)
            throw new IllegalArgumentException("Es necesario brindar los parametros para generar el número");
        parametrosGenerador.setN(1);
        return generar(parametrosGenerador)[0];
    }
}
