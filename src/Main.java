import java.math.RoundingMode;
import java.util.Scanner;
import java.math.BigInteger;
import java.math.BigDecimal;

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
        System.out.println("3.) Probabilitas Kombinasi");
        System.out.println("4.) Bantuan");
        System.out.println("0.) Keluar");
        System.out.print("Pilihan anda : ");

    }

    static boolean program(int pilihan) {
        Scanner input=new Scanner(System.in);
        switch (pilihan) {
            case 1 -> programHgdIndividu();
            case 2 -> programHgdKumulatif();
            case 3 -> {
                System.out.print("Masukkan angka : ");
                int num3 = input.nextInt();
                System.out.println(num3);
            }
            case 4 -> {
                System.out.print("Masukkan angka : ");
                int num4 = input.nextInt();
                System.out.println(num4);
            }
            case 0 -> {
                System.out.println("Sampai Jumpa...");
                System.exit(0);
            }
            default -> System.out.println("Masukkan salah!");
        }
        System.out.print("Apakah anda akan melanjutkan (Y/N) : ");
        String opsi = input.next();

        if (opsi.equalsIgnoreCase("Y")) {
            return true;

        } else {
            return false;

        }

    }

    public static void programHgdIndividu() {
        Scanner input=new Scanner(System.in);

        System.out.print("Masukkan populasi item: ");
        int pops=input.nextInt();
        System.out.print("Masukkan sampel: ");
        int sample=input.nextInt();
        System.out.print("Masukkan jumlah item di populasi: ");
        int success=input.nextInt();
        System.out.print("Masukkan jumlah item yang diinginkan: ");
        int xitem=input.nextInt();

        if(pops>sample){
            BigDecimal res=hypergeomdist(xitem,sample,success,pops);
            double hasil = res.setScale(5, RoundingMode.HALF_UP).doubleValue();
            System.out.println("Besar kemungkinan: " + hasil);
        }
        else System.out.println("Sampel tidak boleh lebih besar dari populasi!");
    }

    public static void programHgdKumulatif() {
        Scanner input=new Scanner(System.in);

        System.out.print("Masukkan populasi item: ");
        int pops=input.nextInt();
        System.out.print("Masukkan sampel: ");
        int sample=input.nextInt();
        System.out.print("Masukkan jumlah item di populasi: ");
        int success=input.nextInt();
        System.out.print("Masukkan maksimal jumlah item yang diinginkan: ");
        int xitem=input.nextInt();

        if(pops>sample){
            BigDecimal res=hgdCumulative(xitem,sample,success,pops);
            double hasil = res.setScale(5, RoundingMode.HALF_UP).doubleValue();
            //double hasil = res.doubleValue();
            System.out.println("Besar kemungkinan: " + hasil);
        }
        else System.out.println("Sampel tidak boleh lebih besar dari populasi!");
    }

    public static BigInteger fact(int n) {
        // int fact = 1;
        BigInteger fact = BigInteger.ONE;

        // Menghitung n faktorial
        // n! = n x (n-1)!

        for (int i = 1; i <= n; ++i) {
            //    fact=fact*i;
            //    fact*=i;
            fact = fact.multiply(BigInteger.valueOf(i));
        }
        return fact;
    }

    public static BigInteger combine(int n, int k) {
        BigInteger comb;

        // Perhitungan fungsi kombinasi (Combination)
        // nCr = ___n!___
        //       k!(n-k)!

        comb = fact(n).divide( fact(n - k).multiply(fact(k)));
        return comb;
    }

    public static BigDecimal hypergeomdist(int x, int sample, int success, int pops) {
        BigInteger topRes;
        BigInteger botRes;
        BigDecimal hgd;

        // Menghitung distribusi Hypergeometrik
        //
        // hgd(x,N,n,k) = [kCx]*[(N-k)C(n-x)]*[NCx]

        // h=combine(success,x)*combine((pops - success),(sample - x))/combine(pops,sample);
        // hgd=combine(success,x).multiply(combine((pops-success),(sample-x)).divide(combine(pops,sample)));
        topRes=combine(success,x).multiply(combine((pops-success),(sample-x)));
        botRes=combine(pops,sample);

        BigDecimal topResD = new BigDecimal(topRes);
        BigDecimal botResD = new BigDecimal(botRes);
        //hgd=topResD.divide(botResD, MathContext.DECIMAL128);
        hgd=topResD.divide(botResD, 5, RoundingMode.HALF_UP);

        return hgd;
    }

    public static BigDecimal hgdCumulative(int x, int sample, int success, int pops) {
        BigDecimal kum = BigDecimal.ZERO;
        for (int i = 0; i <= x; ++i) {
            kum = kum.add(hypergeomdist(i,sample,success,pops));
        }
        kum = kum.setScale(2, RoundingMode.HALF_DOWN);
        //double hasil = kum.doubleValue();
        return kum;
    }

}

