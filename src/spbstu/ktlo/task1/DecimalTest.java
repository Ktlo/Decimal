package spbstu.ktlo.task1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DecimalTest {

    @Test
    void parseDecimalAndToString() {
        assertEquals("0.03459",
                Decimal.parseDecimal(".03459").toString());
        assertEquals("1337",
                Decimal.parseDecimal("1337").toString());
        assertEquals("34.56",
                Decimal.parseDecimal("34.56").toString());
        assertEquals("0",
                Decimal.parseDecimal("+.0").toString());
        assertEquals("0",
                Decimal.parseDecimal("-0.").toString());

        assertEquals("-284761.497480998590686097699690767790379604",
                Decimal.parseDecimal("-284761.497480998590686097699690767790379604").toString());
        assertEquals("-80954624828904782886029867067848637809581656708805371371022001606516567783640848766674614669615.173995036515267488587661553758826499949861786372793183438279071023720148485402057502",
                Decimal.parseDecimal("-80954624828904782886029867067848637809581656708805371371022001606516567783640848766674614669615.173995036515267488587661553758826499949861786372793183438279071023720148485402057502").toString());
    }

    @Test
    void misc() {
        Decimal d = Decimal.parseDecimal("-80954624828904782886029867067848637809581656708805371371022001606516567783640848766674614669615.173995036515267488587661553758826499949861786372793183438279071023720148485402057502");
        assertEquals(84, d.countFractionalDigits());
        assertEquals(95 + 84, d.countDigits());
        assertEquals(3, d.digitAt(21));
        assertEquals(Decimal.parseDecimal("80954624828904782886029867067848637809581656708805371371022001606516567783640848766674614669615.173995036515267488587661553758826499949861786372793183438279071023720148485402057502"),
                d.unaryMinus());
        d = new Decimal(0.125f, 5);
        assertEquals(3, d.countFractionalDigits());
        assertEquals(3, d.countDigits());
        assertEquals(0, d.digitAt(21));
    }

    @Test
    void add() {
        assertEquals(Decimal.parseDecimal("34.56"),
                Decimal.parseDecimal("22.22").add(Decimal.parseDecimal("12.34")));
        assertEquals(Decimal.parseDecimal("1.0"),
                Decimal.parseDecimal(".5").add(Decimal.parseDecimal(".5")));
        assertEquals(Decimal.parseDecimal("2.612"),
                Decimal.parseDecimal("0.912").add(Decimal.parseDecimal("1.7")));
        assertEquals(Decimal.parseDecimal("186.612"),
                Decimal.parseDecimal("99.912").add(Decimal.parseDecimal("86.7")));

        assertEquals(Decimal.parseDecimal("49.48"),
                Decimal.parseDecimal("84.96").add(Decimal.parseDecimal("-35.48")));
        assertEquals(Decimal.parseDecimal("-765"),
                Decimal.parseDecimal("135").add(Decimal.parseDecimal("-900")));
        assertEquals(Decimal.parseDecimal("-0.765"),
                Decimal.parseDecimal("0.135").add(Decimal.parseDecimal("-0.9")));
        assertEquals(Decimal.parseDecimal("-33.17"),
                Decimal.parseDecimal("34.93").add(Decimal.parseDecimal("-68.1")));
        assertEquals(Decimal.parseDecimal("0.9265"),
                Decimal.parseDecimal("0.9885").add(Decimal.parseDecimal("-0.062")));
        assertEquals(Decimal.parseDecimal("8.045"),
                Decimal.parseDecimal("9.135").add(Decimal.parseDecimal("-1.09")));
        assertEquals(Decimal.parseDecimal("9.045"),
                Decimal.parseDecimal("9.135").add(Decimal.parseDecimal("-0.09")));
    }

    @Test
    void sub() {
        assertEquals(Decimal.parseDecimal("34.56"),
                Decimal.parseDecimal("22.22").sub(Decimal.parseDecimal("-12.34")));
        assertEquals(Decimal.parseDecimal("1.0"),
                Decimal.parseDecimal(".5").sub(Decimal.parseDecimal("-.5")));
        assertEquals(Decimal.parseDecimal("2.612"),
                Decimal.parseDecimal("0.912").sub(Decimal.parseDecimal("-1.7")));
        assertEquals(Decimal.parseDecimal("186.612"),
                Decimal.parseDecimal("99.912").sub(Decimal.parseDecimal("-86.7")));

        assertEquals(Decimal.parseDecimal("49.48"),
                Decimal.parseDecimal("84.96").sub(Decimal.parseDecimal("35.48")));
        assertEquals(Decimal.parseDecimal("-765"),
                Decimal.parseDecimal("135").sub(Decimal.parseDecimal("900")));
        assertEquals(Decimal.parseDecimal("-0.765"),
                Decimal.parseDecimal("0.135").sub(Decimal.parseDecimal("0.9")));
        assertEquals(Decimal.parseDecimal("-33.17"),
                Decimal.parseDecimal("34.93").sub(Decimal.parseDecimal("68.1")));
        assertEquals(Decimal.parseDecimal("0.9265"),
                Decimal.parseDecimal("0.9885").sub(Decimal.parseDecimal("0.062")));
        assertEquals(Decimal.parseDecimal("8.045"),
                Decimal.parseDecimal("9.135").sub(Decimal.parseDecimal("1.09")));
        assertEquals(Decimal.parseDecimal("9.045"),
                Decimal.parseDecimal("9.135").sub(Decimal.parseDecimal("0.09")));
    }

    @Test
    void multiply() {
        assertEquals(Decimal.parseDecimal("-291233.2401384"),
                Decimal.parseDecimal("5846.6482").multiply(Decimal.parseDecimal("-49.812")));
        assertEquals(Decimal.parseDecimal("2"),
                Decimal.parseDecimal("-.4").multiply(Decimal.parseDecimal("-5")));
    }

    @Test
    void round() {
        assertEquals(Decimal.parseDecimal("4725"),
                Decimal.parseDecimal("4725.4825").round());
        assertEquals(Decimal.parseDecimal("4725.48"),
                Decimal.parseDecimal("4725.4825").round(-2));
        assertEquals(Decimal.parseDecimal("4700"),
                Decimal.parseDecimal("4725.4825").round(2));
        assertEquals(Decimal.parseDecimal("1000"),
                Decimal.parseDecimal("999.6").round());
    }

    @Test
    void constructorFloat() {
        assertEquals(Decimal.parseDecimal("0.125"), new Decimal(0.125f, 5));
        assertEquals(Decimal.parseDecimal("0.13"), new Decimal(0.125, 2));
        assertEquals(Decimal.parseDecimal("475.13"), new Decimal(475.13f, 2));
        assertEquals(Decimal.parseDecimal("-10"), new Decimal(-9.96, 1));
    }

    @Test
    void constructorInt() {
        assertEquals(Decimal.parseDecimal("312845"), new Decimal(312845));

        assertEquals(Decimal.parseDecimal(Integer.toString(Integer.MAX_VALUE)),
                new Decimal(Integer.MAX_VALUE));
        assertEquals(Decimal.parseDecimal(Integer.toString(Integer.MIN_VALUE + 1)),
                new Decimal(Integer.MIN_VALUE + 1));

        assertEquals(Decimal.parseDecimal("312845365836609770"), new Decimal(312845365836609770L));

        assertEquals(Decimal.parseDecimal(Long.toString(Long.MAX_VALUE)),
                new Decimal(Long.MAX_VALUE));
        assertEquals(Decimal.parseDecimal(Long.toString(Long.MIN_VALUE + 1)),
                new Decimal(Long.MIN_VALUE + 1));
    }

    @Test
    void intValue() {
        assertEquals(23, Decimal.parseDecimal("23.458").intValue());
    }

    @Test
    void longValue() {
        assertEquals(-468784768376990L, Decimal.parseDecimal("-468784768376990.82887").longValue());
    }

    @Test
    void doubleValue() {
        assertEquals(0.125, Decimal.parseDecimal("0.125").doubleValue());
        assertEquals(-0.125, Decimal.parseDecimal("-0.125").doubleValue());
    }

    @Test
    void compareTo() {
        assertEquals(0,
                Decimal.parseDecimal("482964.0488786").compareTo(Decimal.parseDecimal("482964.0488786")));
        assertEquals(0,
                Decimal.parseDecimal("482964.048878600").compareTo(Decimal.parseDecimal("00482964.0488786")));
        assertEquals(-1,
                Decimal.parseDecimal("482964.0488776").compareTo(Decimal.parseDecimal("482964.0488786")));
        assertEquals(-1,
                Decimal.parseDecimal("482964.0488786").compareTo(Decimal.parseDecimal("482964.0489786")));
        assertEquals(1,
                Decimal.parseDecimal("482964.0488786").compareTo(Decimal.parseDecimal("482964.0488686")));
        assertEquals(1,
                Decimal.parseDecimal("482964.06").compareTo(Decimal.parseDecimal("482964.0488686")));
        assertEquals(-1,
                Decimal.parseDecimal("-482964.06").compareTo(Decimal.parseDecimal("482964.0488686")));
        assertEquals(0,
                Decimal.parseDecimal("+0.0000").compareTo(Decimal.parseDecimal("-000000.000")));
    }

    @Test
    void equalsTest() {
        assertEquals(Decimal.parseDecimal("-93.6288"), Decimal.parseDecimal("-93.6288"));
        assertNotEquals(Decimal.parseDecimal("93.6288"), Decimal.parseDecimal("-93.6288"));
    }

    @Test
    void hashCodeTest() {
        assertNotEquals(Decimal.parseDecimal("-80954624828904782886029867067848637809581656708805371371022001606516567783640848766674614669615.9696757629569797").hashCode(),
                Decimal.parseDecimal("946.462576"));
        assertEquals(Decimal.parseDecimal("-80954624828904782886029867067848637809581656708805371371022001606516567783640848766674614669615.9696757629569797").hashCode(),
                Decimal.parseDecimal("-80954624828904782886029867067848637809581656708805371371022001606516567783640848766674614669615.9696757629569797").hashCode());
        assertEquals(Decimal.parseDecimal("946.462576").hashCode(), Decimal.parseDecimal("946.462576").hashCode());
    }
}