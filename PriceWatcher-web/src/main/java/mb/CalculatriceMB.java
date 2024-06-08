package mb;

import java.io.Serializable;
import java.text.NumberFormat;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

/**
 *
 * @author Lucien FOTSA
 */
public class CalculatriceMB implements Serializable {

    private String view = "";
    private String viewInter = "0";
    private String reset = "";
    private String optr = " + ";
    private float opde1 = 0F;
    private float opde2 = 0F;
    private float result = 0F;
    private float opdeInter = 0F;
    private int step = 1;
    private final NumberFormat nf = NumberFormat.getInstance(FacesContext.getCurrentInstance().getViewRoot().getLocale());

    @PostConstruct
    public void init() {
    }

    public CalculatriceMB() {
    }

    public void Reset() {
        this.opde1 = 0F;
        this.opde2 = 0F;
        this.result = 0F;
        this.opdeInter = 0F;
        reset = "";
        view = "";
        viewInter = "0";
        optr = " + ";
        step = 1;
    }

    public void cls() {
        if (!reset.isEmpty()) {
            Reset();
        }
    }

//    public String format(String chaine) {
//        int pas = 3;
//        String rest = "";
//        if (chaine.contains(".")) {
//            rest = chaine.substring(chaine.indexOf('.'));
//            chaine = chaine.substring(0, chaine.indexOf('.'));
//        }
//        while (chaine.length() - pas > 0) {
//            chaine = chaine.substring(0, chaine.length() - pas) + " " + chaine.substring(chaine.length() - pas, chaine.length());
//            pas += 4;
//        }
//        chaine += rest.equals(".0") ? "" : rest;
//        return chaine;
//    }
    public float calculer(float opde1, String optr, float opde2) {
        if (optr.equals(" + ")) {
            result = opde1 + opde2;
        }
        if (optr.equals(" - ")) {
            result = opde1 - opde2;
        }
        if (optr.equals(" / ")) {
            result = opde1 / opde2;
        }
        if (optr.equals(" x ")) {
            result = opde1 * opde2;
        }
        if (optr.equals(" mod ")) {
            result = opde1 % opde2;
        }
        if (optr.equals(" expo ")) {
            result = (float) Math.pow(opde1, opde2);
        }
        return result;
    }

    public void read1() {
        cls();
        viewInter += "1";
        view += "1";
        step = 1;
    }

    public void read2() {
        cls();
        viewInter += "2";
        view += "2";
        step = 1;
    }

    public void read3() {
        cls();
        viewInter += "3";
        view += "3";
        step = 1;
    }

    public void read4() {
        cls();
        viewInter += "4";
        view += "4";
        step = 1;
    }

    public void read5() {
        cls();
        viewInter += "5";
        view += "5";
        step = 1;
    }

    public void read6() {
        cls();
        viewInter += "6";
        view += "6";
        step = 1;
    }

    public void read7() {
        cls();
        viewInter += "7";
        view += "7";
        step = 1;
    }

    public void read8() {
        cls();
        viewInter += "8";
        view += "8";
        step = 1;
    }

    public void read9() {
        cls();
        viewInter += "9";
        view += "9";
        step = 1;
    }

    public void read0() {
        cls();
        viewInter += "0";
        view += "0";
        step = 1;
    }

    public void readComa() {
        cls();
        viewInter += ".";
        view += ".";
        step = 1;
    }

    public void readReset() {
        Reset();
    }

    public void readDivide() {
        opde1 = calculer(opde1, optr, Float.parseFloat(String.valueOf(viewInter)));
        view = nf.format(opde1) + " / ";
        viewInter = "0";
        optr = " / ";
        step = 2;
        reset = "";
    }

    public void readMultiply() {
        opde1 = calculer(opde1, optr, Float.parseFloat(String.valueOf(viewInter)));
        view = nf.format(opde1) + " x ";
        viewInter = "0";
        optr = " x ";
        step = 2;
        reset = "";
    }

    public void readSubstract() {
        opde1 = calculer(opde1, optr, Float.parseFloat(String.valueOf(viewInter)));
        view = nf.format(opde1) + " - ";
        viewInter = "0";
        optr = " - ";
        step = 2;
        reset = "";
    }

    public void readAdd() {
        opde1 = calculer(opde1, optr, Float.parseFloat(String.valueOf(viewInter)));
        view = nf.format(opde1) + " + ";
        viewInter = "0";
        optr = " + ";
        step = 2;
        reset = "";
    }

    public void readEqual() {
        opde1 = calculer(opde1, optr, Float.parseFloat(String.valueOf(viewInter)));
        view = nf.format(opde1);
        viewInter = "0";
        reset = "ok";
        optr = " + ";
    }

    public void readModulo() {
        opde1 = calculer(opde1, optr, Float.parseFloat(String.valueOf(viewInter)));
        view = nf.format(opde1) + " mod ";
        viewInter = "0";
        optr = " mod ";
        step = 4;
        reset = "";
    }

    public void readExpo() {
        opde1 = calculer(opde1, optr, Float.parseFloat(String.valueOf(viewInter)));
        view = nf.format(opde1) + " pow ";
        viewInter = "0";
        optr = " expo ";
        step = 5;
        reset = "";
    }

    public float getOpde1() {
        return opde1;
    }

    public void setOpde1(float opde1) {
        this.opde1 = opde1;
    }

    public float getOpde2() {
        return opde2;
    }

    public void setOpde2(float opde2) {
        this.opde2 = opde2;
    }

    public float getOpdeInter() {
        return opdeInter;
    }

    public void setOpdeInter(float opdeInter) {
        this.opdeInter = opdeInter;
    }

    public float getResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getOptr() {
        return optr;
    }

    public void setOptr(String optr) {
        this.optr = optr;
    }

    public String getViewInter() {
        return viewInter;
    }

    public void setViewInter(String viewInter) {
        this.viewInter = viewInter;
    }
}
