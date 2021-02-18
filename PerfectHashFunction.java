/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class PerfectHashFunction {
    public static void main(String[] args) {
        String example = "S E A R C H X M P L";
        int a = 1; // (a*k) % m = a*(k%m), so the value of a does not matter; set to 1
        int m = 10; // the minimum value for m is the length of the string, which in our case is 10

        // convert to the argument from the alphabet to keys
        Character[] alphabet = new Character[10];
        int[] numbers = new int[10];

        int alphabetIndex = 0;
        for (int i = 0; i < 19; i++) {
            if (example.charAt(i) != ' ') {
                alphabet[alphabetIndex] = example.charAt(i);
                alphabetIndex++;
            }
        }
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = alphabet[i] - 64;
        }

        // find the smallest value of m
        boolean uniqueHash = false;
        while (!uniqueHash) {
            boolean breakLoop = false;
            // loop through the converted alphabet loop and find if any two have the same value
            for (int i = 0; i < numbers.length - 1; i++) {
                for (int j = i + 1; j < numbers.length; j++) {
                    // if two values are the same break, increment m
                    if ((numbers[i] % m) == (numbers[j] % m)) {
                        m++;
                        breakLoop = true;
                        break;
                    }
                }
            }
            if (!breakLoop) {
                uniqueHash = true;
            }
        }

        System.out.println("The smallest value of m is " + m + " and a is " + a);

    }
}

/*
OUTPUT:
The smallest value of m is 20 and a is 1
 */
