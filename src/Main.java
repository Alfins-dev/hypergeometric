import java.lang.reflect.Array;
import java.util.Scanner;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Main {
    public static void main(String[] args) {
        Scanner input=new Scanner(System.in);

        boolean loop = true;

        while (loop) {
            menu();
            int pilih=input.nextInt();
            loop=program(pilih);

        }

    }

    static void menu() {
        System.out.println("==================");
        System.out.println("== Menu Program ==");
        System.out.println("==================");
        System.out.println("1.) Probabilitas Individu");
        System.out.println("2.) Probabilitas Kumulatif");
//        System.out.println("3.) Debug");
        System.out.println("0.) Keluar");
        System.out.print("Pilihan anda : ");

    }

    static boolean program(int pilihan) {
        Scanner input=new Scanner(System.in);
        switch (pilihan) {
            case 1 -> programHgdIndividu();
            case 2 -> programHgdKumulatif();
            case 3 -> prosesInput();
            case 0 -> {
                System.out.println("Sampai Jumpa...");
                System.exit(0);
            }
            default -> System.out.println("Maaf, masukkan anda salah!");
        }
        System.out.print("Apakah anda akan melanjutkan (Y/N) : ");
        String opsi = input.next();

        return opsi.equalsIgnoreCase("Y");

    }

    public static int[] prosesInput() {
        Scanner input=new Scanner(System.in);
        int[] arrInput = new int[4];

        System.out.print("Masukkan populasi item: ");
        arrInput[3]=input.nextInt();
        System.out.print("Masukkan sampel: ");
        arrInput[1]=input.nextInt();
        System.out.print("Masukkan jumlah item di populasi: ");
        arrInput[2]=input.nextInt();
        System.out.print("Masukkan jumlah item yang diinginkan: ");
        arrInput[0]=input.nextInt();

        return arrInput;

//        System.out.println(Arrays.toString(arrInput));
    }

    // buat jadi array
    public static boolean checkInput(int[] arrInput) {
        int x = (int)Array.get(arrInput, 0);
        int sample = (int)Array.get(arrInput, 1);
        int success = (int)Array.get(arrInput, 2);
        int pops = (int)Array.get(arrInput, 3);

        if(x > sample) {
            System.out.println("Jumlah item tidak boleh lebih besar dari sampel!");
            return false;
        } else if (sample > pops) {
            System.out.println("Jumlah sampel tidak boleh lebih besar dari populasi!");
            return false;
        } else if (success > pops) {
            System.out.println("Jumlah item dalam populasi tidak boleh lebih besar dari populasi!");
            return false;
        } else {
            return true;
        }

//    return x < sample && sample < success && success < pops;
    }

    public static void programHgdIndividu() {

        while (true) {
            int[] arrInput;
            arrInput = prosesInput();
            int xitem = (int)Array.get(arrInput, 0);
            int sample = (int)Array.get(arrInput, 1);
            int success = (int)Array.get(arrInput, 2);
            int pops = (int)Array.get(arrInput, 3);

            if(checkInput(arrInput)){
                BigDecimal res=hypergeomdist(xitem,sample,success,pops);
                double hasil = res.doubleValue();
                System.out.println("Besar kemungkinan: " + hasil);
                break;
            }
            System.out.println("Input salah!");
        }

    }

    public static void programHgdKumulatif() {

        while (true) {
            int[] arrInput;
            arrInput = prosesInput();
            int xitem = (int) Array.get(arrInput, 0);
            int sample = (int)Array.get(arrInput, 1);
            int success = (int)Array.get(arrInput, 2);
            int pops = (int)Array.get(arrInput, 3);

            if(checkInput(arrInput)){
                BigDecimal res=hgdCumulative(xitem,sample,success,pops);
                double hasil = res.doubleValue();
                System.out.println("Besar kemungkinan: " + hasil);
                break;
            }
            System.out.println("Input salah!");
        }
    }

    // Menghitung n faktorial
    // n! = n x (n-1)!

    public static BigInteger fact(int n) {
        // int fact = 1;
        BigInteger fact = BigInteger.ONE;

        for (int i = 1; i <= n; ++i) {
            //    fact=fact*i;
            //    fact*=i;
            fact = fact.multiply(BigInteger.valueOf(i));
        }
        return fact;
    }

    // Perhitungan fungsi kombinasi (Combination)
    // nCr = ___n!___
    //       k!(n-k)!

    public static BigInteger combine(int n, int k) {
        BigInteger comb;

        comb = fact(n).divide( fact(n - k).multiply(fact(k)));
        return comb;
    }

    // Menghitung distribusi Hypergeometrik
    //
    //                 [kCx]*[(N-k)C(n-x)]  < topRes
    // hgd(x,N,n,k) =  ___________________
    //                       [NCx]          < botRes

    public static BigDecimal hypergeomdist(int x, int sample, int success, int pops) {
        BigInteger topRes;
        BigInteger botRes;
        BigDecimal hgd;

        // h=combine(success,x)*combine((pops - success),(sample - x))/combine(pops,sample);
        // hgd=combine(success,x).multiply(combine((pops-success),(sample-x)).divide(combine(pops,sample)));
        topRes=combine(success,x).multiply(combine((pops-success),(sample-x)));
        botRes=combine(pops,sample);

        BigDecimal topResD = new BigDecimal(topRes);
        BigDecimal botResD = new BigDecimal(botRes);
        hgd=topResD.divide(botResD, 5, RoundingMode.HALF_UP);
//        hgd = hgd.setScale( 5, RoundingMode.HALF_UP);
        return hgd;
    }

    public static BigDecimal hgdCumulative(int x, int sample, int success, int pops) {
        BigDecimal kum = BigDecimal.ZERO;
        for (int i = 0; i <= x; ++i) {
            kum = kum.add(hypergeomdist(i,sample,success,pops));
        }
        kum = kum.setScale(5, RoundingMode.HALF_UP);
        return kum;
    }

}

