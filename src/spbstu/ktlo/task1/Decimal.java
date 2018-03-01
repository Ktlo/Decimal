package spbstu.ktlo.task1;

import java.io.ByteArrayOutputStream;

public class Decimal extends Number implements Comparable<Decimal> {

    static private final byte[] EMPTY_ARRAY = new byte[0];

    private final byte[] data;
    private final int fractionalDigitsN;
    private final boolean minusSign;
    private final int realLength;

    private Decimal(byte[] data, int offset, boolean sign) {
        this.data = data;
        fractionalDigitsN = offset;
        minusSign = sign;
        realLength = computeLength();
    }

    public Decimal() {
        data = EMPTY_ARRAY;
        fractionalDigitsN = 0;
        minusSign = false;
        realLength = 0;
    }

    public Decimal(int value) {
        data = new byte[10];
        fractionalDigitsN = 0;
        if (minusSign = value < 0)
            value = -value;
        for (int i = 0; value > 0; i++) {
            data[i] = (byte)(value % 10);
            value /= 10;
        }
        realLength = computeLength();
    }

    public Decimal(long value) {
        data = new byte[19];
        fractionalDigitsN = 0;
        if (minusSign = value < 0)
            value = -value;
        for (int i = 0; value > 0; i++) {
            data[i] = (byte)(value % 10);
            value /= 10;
        }
        realLength = computeLength();
    }

    public Decimal(float value, int fractionalDigitsNumber) {
        this((double)value, fractionalDigitsNumber);
    }

    public Decimal(double value, int fractionalDigitsN) {
        if (minusSign = value < 0)
            value = -value;
        double fractional = value % 1;
        value -= fractional;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int countAll = 0;
        int countFractional = 0;
        byte tmp;

        byte[] newData = new byte[fractionalDigitsN];
        // Read fractional part
        for (; fractional > 0 && countFractional < fractionalDigitsN; countFractional++) {
            fractional *= 10;
            tmp = (byte)fractional;
            fractional -= tmp;
            newData[countFractional] = tmp;
        }

        // Inverse
        int n = countFractional / 2;
        int i;
        for (i = 0; i < n; i++) {
            tmp = newData[i];
            newData[i] = newData[countFractional - i - 1];
            newData[countFractional - i - 1] = tmp;
        }

        // Write fractional part
        buffer.write(newData, 0, countFractional);

        // Write integer part
        for (; value > 0; countAll++) {
            tmp = (byte)(value % 10);
            value -= tmp;
            value /= 10;
            buffer.write(tmp);
        }
        buffer.write(0);

        int length = countFractional + countAll;
        newData = buffer.toByteArray();

        // Round
        if (fractional >= .5) {
            i = 0;
            while (newData[i] == 9) {
                newData[i] = 0;
                i++;
            }
            newData[i]++;
            if (i == length)
                length++;
            if (i > 0) {
                if (i > countFractional)
                    i = countFractional;
                byte[] veryNewData = new byte[length - Math.min(i, countFractional)];
                System.arraycopy(newData, i, veryNewData, 0, veryNewData.length);
                newData = veryNewData;
                countFractional -= i;
                length = newData.length;
            }
        }
        data = newData;
        this.fractionalDigitsN = countFractional;
        realLength = length;
        // buffer.close(); -- This has no effect.
    }

    private boolean amIAZero() {
        for (int i = realLength - 1; i >= 0; i--)
            if (data[i] != 0)
                return false;
        return true;
    }

    private int computeLength() {
        int realLength = data.length - 1;
        while (realLength >= fractionalDigitsN && data[realLength] == 0)
            realLength--;
        realLength++;
        return realLength;
    }

    private static int allocateAmount(String value) throws NumberFormatException {
        int n = 0,
            length = value.length();
        boolean hasNotDot = true;
        for (int i = 0; i < length; i++) {
            char current = value.charAt(i);
            if (current <= '9' && current >= '0')
                n++;
            else if (current == '.' && hasNotDot)
                hasNotDot = false;
            else if (i > 0 || current != '+' && current != '-')
                throw new NumberFormatException(
                    "Unexpected character at " + Integer.toString(i) + ": '" + current + '\''
                );
        }
        if (n == 0)
            throw new NumberFormatException("Not a decimal");

        // Cut zeros
        int i = length - 1;
        if (!hasNotDot)
            for (i = length - 1; value.charAt(i) == '0';)
                i--;
        return n - length + 1 + i;
    }

    public static Decimal parseDecimal(String value) throws NumberFormatException {
        if (value == null)
            throw new NullPointerException();
        byte[] data = new byte[allocateAmount(value)];
        int i = 0, j;
        char current = value.charAt(i);
        boolean sign = current == '-';
        if (sign || current == '+')
            i++;
        int length = value.length();
        boolean hasFractionalDigits = false;

        // Cut zeros
        for (j = data.length - 1; i < length; i++, j--) {
            current = value.charAt(i);
            if (current == '.') {
                hasFractionalDigits = true;
                break;
            }
            data[j] = (byte)(current - '0');
        }
        int point = i;
        if (hasFractionalDigits) {
            point = ++i;
            for (; j >= 0; i++, j--)
                data[j] = (byte)(value.charAt(i) - '0');
        }
        return new Decimal(data, i - point, sign);
    }

    public int countFractionalDigits() {
        return fractionalDigitsN;
    }

    public int countDigits() {
        return realLength;
    }

    public Decimal unaryMinus() {
        return new Decimal(data, fractionalDigitsN, !minusSign);
    }

    public int digitAt(int index) {
        if (index > realLength - fractionalDigitsN)
            return 0;
        else if (-index > fractionalDigitsN)
            return 0;
        else
            return data[index + fractionalDigitsN];
    }

    public Decimal round() {
        return round(0);
    }

    public Decimal round(int index) {
        int normalizedIndex = index + fractionalDigitsN;
        if (normalizedIndex > realLength)
            throw new IllegalArgumentException("Index is too big: " + Integer.toString(index));
        if (normalizedIndex <= 0)
            return this;
        byte[] newData = new byte[realLength - Math.min(normalizedIndex, fractionalDigitsN) + 1];
        int offset = index > 0 ? index : 0;
        System.arraycopy(data, normalizedIndex, newData, offset, realLength - normalizedIndex);
        normalizedIndex--;
        if (data[normalizedIndex] >= 5)
            newData[offset]++;
        for (; newData[offset] == 10; offset++) {
            newData[offset + 1]++;
            newData[offset] = 0;
        }

        return new Decimal(newData, index > 0 ? 0 : -index, minusSign);
    }

    private Decimal addSameSign(Decimal other) {
        Decimal a, b;
        if (fractionalDigitsN > other.fractionalDigitsN) {
            a = this;
            b = other;
        }
        else {
            a = other;
            b = this;
        }
        int i;
        byte[] newData = new byte[Math.max(a.realLength, b.realLength) + 1];
        int offset = a.fractionalDigitsN - b.fractionalDigitsN;
        // Add to empty fractional space
        for (i = 0; i < offset; i++)
            newData[i] = a.data[i];

        int tmp = 0; // Next extra bit
        int minLength = Math.min(a.realLength
                , b.realLength + a.fractionalDigitsN - b.fractionalDigitsN);
        // Add to the common registry space
        for (; i < minLength; i++) {
            tmp = a.data[i] + b.data[i - offset] + tmp;
            if (tmp >= 10) {
                newData[i] = (byte)(tmp - 10);
                tmp = 1;
            }
            else {
                newData[i] = (byte)tmp;
                tmp = 0;
            }
        }

        // Add the last integer space
        if (a.realLength - a.fractionalDigitsN > b.realLength - b.fractionalDigitsN) {
            for (; i < a.realLength; i++) {
                tmp = a.data[i] + tmp;
                if (tmp >= 10) {
                    newData[i] = (byte)(tmp - 10);
                    tmp = 1;
                }
                else {
                    newData[i] = (byte)tmp;
                    tmp = 0;
                }
            }
        }
        else {
            for (i -= offset; i < b.realLength; i++) {
                tmp = b.data[i] + tmp;
                if (tmp >= 10) {
                    newData[i + offset] = (byte)(tmp - 10);
                    tmp = 1;
                }
                else {
                    newData[i + offset] = (byte)tmp;
                    tmp = 0;
                }
            }
        }

        // Add the last register
        if (tmp > 0)
            newData[newData.length - 1] = 1;

        return new Decimal(newData, a.fractionalDigitsN, minusSign);
    }

    private Decimal addDifferentSign(Decimal other, boolean otherSign) {
        Decimal a, b;
        int tmp = compareWithoutSign(other);
        if (tmp > 0) {
            a = this;
            b = other;
            otherSign = this.minusSign;
        }
        else if (tmp < 0) {
            a = other;
            b = this;
        }
        else
            return new Decimal();
        int offset, beginIndex, endIndex, i, newDataOffset;
        byte[] newData = new byte[Math.max(realLength, other.realLength) + 1];
        if (b.fractionalDigitsN > a.fractionalDigitsN) {
            // Sub b's tale from the next of tale registry
            tmp = 1;
            offset = b.fractionalDigitsN - a.fractionalDigitsN;
            for (i = 0; i < offset; i++) {
                newData[i] = (byte) (9 - b.data[i]);
            }
            newData[0]++;
            beginIndex = 0;
            endIndex = b.realLength - offset;
            newDataOffset = offset;
            //offset = -offset;
        }
        else {
            // Add a's tale to the end of new number
            tmp = 0;
            offset = a.fractionalDigitsN - b.fractionalDigitsN;
            for (i = 0; i < offset; i++) {
                newData[i] = a.data[i];
            }
            beginIndex = offset;
            endIndex = b.realLength + offset;
            newDataOffset = 0;
            offset = -offset;
        }

        // Main substitution
        for (i = beginIndex; i < endIndex; i++) {
            tmp = a.data[i] - b.data[i + offset] - tmp;
            if (tmp < 0) {
                newData[i + newDataOffset] = (byte)(tmp + 10);
                tmp = 1;
            }
            else {
                newData[i + newDataOffset] = (byte)tmp;
                tmp = 0;
            }
        }
        offset = Math.max(fractionalDigitsN, other.fractionalDigitsN);

        // Completing the greatest registers
        for (; i < a.realLength; i++) {
            tmp = a.data[i] - tmp;
            if (tmp < 0) {
                newData[i + newDataOffset] = (byte)(tmp + 10);
                tmp = 1;
            }
            else {
                newData[i + newDataOffset] = (byte)tmp;
                i++;
                break;
            }
        }

        // Copy the last digits from a
        for (; i < a.realLength; i++) {
            newData[i + beginIndex] = a.data[i];
        }

        // Are there any unnecessary digits?
        beginIndex = 0;
        while (beginIndex < offset && newData[beginIndex] == 0)
            beginIndex++;
        if (beginIndex > 0) {
            // OK. Shrink it.
            endIndex = newData.length - 1;
            while (endIndex > fractionalDigitsN)
                endIndex--;
            if (endIndex == beginIndex)
                return new Decimal();
            byte[] veryNewData = new byte[endIndex - beginIndex];
            offset -= beginIndex;
            System.arraycopy(newData, beginIndex, veryNewData, 0, veryNewData.length);
            newData = veryNewData;
        }

        return new Decimal(newData, offset, otherSign);
    }

    public Decimal add(Decimal other) {
        if (other.minusSign == minusSign)
            return addSameSign(other);
        else
            return addDifferentSign(other, other.minusSign);
    }

    public Decimal sub(Decimal other) {
        if (other.minusSign == minusSign)
            return addDifferentSign(other, !other.minusSign);
        else
            return addSameSign(other);
    }

    public Decimal multiply(Decimal other) {
        byte[] newData = new byte[realLength + other.realLength];
        int i, j;
        int tmp;
        for (i = 0; i < realLength; i++) {
            tmp = data[i];
            if (tmp > 0) {
                for (j = 0; j < other.realLength; j++)
                    newData[i + j] += tmp * other.data[j];
                for (j = 1; j < newData.length; j++) {
                    tmp = newData[j - 1];
                    newData[j] += tmp / 10;
                    newData[j - 1] = (byte)(tmp % 10);
                }
            }
        }

        int newFractionalDigitsN = fractionalDigitsN + other.fractionalDigitsN;
        for (i = 0; i < newFractionalDigitsN && newData[i] == 0;)
            i++;
        if (i > 0) {
            byte[] veryNewData = new byte[newData.length - i];
            System.arraycopy(newData, i, veryNewData, 0, veryNewData.length);
            newData = veryNewData;
            newFractionalDigitsN -= i;
        }

        return new Decimal(newData, newFractionalDigitsN, minusSign != other.minusSign);
    }

    @Override
    public int intValue() {
        if (realLength - fractionalDigitsN <= 10) {
            int value = 0;
            for (int i = realLength - 1; i >= fractionalDigitsN; i--) {
                value = value * 10 + data[i];
            }
            if (value >= 0)
                return minusSign ? -value : value;
        }
        return minusSign ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }

    @Override
    public long longValue() {
        if (realLength - fractionalDigitsN <= 19) {
            long value = 0;
            for (int i = realLength - 1; i >= fractionalDigitsN; i--) {
                value = value * 10 + data[i];
            }
            if (value >= 0)
                return minusSign ? -value : value;
        }
        return minusSign ? Long.MIN_VALUE : Long.MAX_VALUE;
    }

    @Override
    public float floatValue() {
        return (float)doubleValue();
    }

    @Override
    public double doubleValue() {
        double fractional = .0;
        int i;
        for (i = 0; i < fractionalDigitsN; i++) {
            fractional += data[i];
            fractional /= 10;
        }
        double integer = .0;
        for (i = realLength - 1; i >= fractionalDigitsN; i--) {
            integer *= 10;
            integer += data[i];
        }
        return minusSign ? -(integer + fractional) : integer + fractional;
    }

    private int compareWithoutSign(Decimal other) {
        int intLengthThis = realLength - fractionalDigitsN;
        int intLengthOther = other.realLength - other.fractionalDigitsN;
        if (intLengthThis > intLengthOther)
            return 1;
        else if (intLengthThis < intLengthOther)
            return -1;
        else {
            Decimal a, b;
            boolean changed;
            if (realLength > other.realLength) {
                a = this;
                b = other;
                changed = false;
            }
            else {
                a = other;
                b = this;
                changed = true;
            }
            int offset = a.realLength - b.realLength;
            int i;
            for (i = a.realLength - 1; i >= offset; i--) {
                int tmp = a.data[i] - b.data[i - offset];
                if (tmp > 0)
                    return changed ? -1 : 1;
                if (tmp < 0)
                    return changed ? 1 : -1;
            }
            if (fractionalDigitsN == other.fractionalDigitsN)
                return 0;
            for (; i >= 0; i--) {
                if (a.data[i] > 0)
                    return changed ? -1 : 1;
            }
            return 0;
        }
    }

    @Override
    public int compareTo(Decimal other) {
        if (amIAZero() && other.amIAZero())
            return 0;
        else if (minusSign && !other.minusSign)
            return -1;
        else if (!minusSign && other.minusSign)
            return 1;
        else
            return minusSign ? -compareWithoutSign(other)
                             : compareWithoutSign(other);
    }

    @Override
    public String toString() {
        if (amIAZero())
            return "0";
        char[] outStr = new char[realLength + 3];
        int index = realLength + 2;
        int i = 0;
        if (fractionalDigitsN > 0) {
            for (; i < fractionalDigitsN; i++, index--)
                outStr[index] = (char)(data[i] + '0');
            outStr[index--] = '.';
        }
        for (; i < realLength; i++, index--)
            outStr[index] = (char)(data[i] + '0');
        if (realLength == fractionalDigitsN)
            outStr[index--] = '0';
        if (minusSign)
            outStr[index--] = '-';
        index++;
        return new String(outStr, index, outStr.length - index);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof Decimal && ((Decimal) other).compareTo(this) == 0;
    }

    @Override
    public int hashCode() {
        int sum = 0;
        int length = realLength - realLength % 4;
        for (int i = 0; i < length; i += 4) {
            sum ^= ((int)data[i] << 24) | ((int)data[i + 1] << 16)
                    | ((int)data[i + 2] << 8) | (int)data[i + 3];
            sum = sum << 4 | sum >> 28;
        }
        int last = 0;
        if (length < realLength)
            last |= (int)data[length] << 24;
        length++;
        if (length < realLength)
            last |= (int)data[length] << 16;
        length++;
        if (length < realLength)
            last |= (int)data[length] << 8;
        sum ^= last + fractionalDigitsN;
        return minusSign ? ~sum : sum;
    }
}
