package tictactoe_3d;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * TICTACTOE_3D con un solo nivel de recursion, optimizado para que tome en
 * cuenta las posible jugadas que sean ganadoras.
 * @author vicente santacoloma 08-11044
 */
public class TICTACTOE_3D {

    private char table [][][];
    private char human;
    private char ai;
    private char actualPlayer;
    private char againstPlayer;
    private boolean begins;
    
    public TICTACTOE_3D () {
        this.table = new char [4][4][4];
        this.actualPlayer = ' ';
        this.againstPlayer = ' ';
        this.human = ' ';
        this.ai = ' '; 
        this.begins = true;
    }
    
    /*
     * Inicializa el juego dejando todas las casillas del tablero con '-'
     */
    private void initialize_game () {
        
        for(int k = 0; k<4; k++) {
            for(int i = 0; i<4; i++) {
                for(int j = 0; j<4; j++) {
                    this.table[k][i][j] = '-';
                }
            }
        }
    }
    
    /*
     * Da la opcion al jugador de seleccionar la pieza 'X' o la pieza 'O' para
     * ser utilizada en el juego
     */
    private void select_piece () throws IOException {
        
        boolean b =true;
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        while(b) {
            System.out.println("Select: 1.Axis or 2.Zero");
            int c = Integer.parseInt(read.readLine());
            if (c == 1) {
                this.human = 'X';
                this.ai = 'O';
                b = false;
            } else if (c == 2) {
                this.human = 'O';
                this.ai = 'X';
                b = false;
            } else {
                System.out.println("Invalid Piece, Select Again");
            }
        } 
    }
    
    /*
     * Seleccion el jugador(human o ai) a iniciar la partida
     */
    private void select_first_play() throws IOException {
        
        boolean b =true;
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        while(b) {
            System.out.println("Who begins? 1.Human 2.AI");
            int c = Integer.parseInt(read.readLine());
            if (c == 1) {
                this.begins = false;
                b = false;
            } else if (c == 2) {
                this.begins = true;
                b = false;
            } else {
                System.out.println("Invalid Information, Select Again");
            }
        } 
    }
    
    /*
     * Selecciona la posicion a jugar eligiendo el tablero, la fila y la 
     * columna
     */
    private void select_position () throws IOException {
        
        boolean b = true;
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        
        while(b) {
            System.out.println("Select Table (0-3): ");
            int t = Integer.parseInt(read.readLine());
            System.out.println("Select File (0-3): ");
            int f = Integer.parseInt(read.readLine());
            System.out.println("Select Column (0-3): ");
            int c = Integer.parseInt(read.readLine());
            if((0 <= t && t < 4) && (0 <= f && f < 4) && (0 <= c && c < 4)) {
                if(this.table[t][f][c] == '-') {
                    this.table[t][f][c] = this.human;
                    b = false;        
                }
                else
                    System.out.println("Already Played Position, Select Again!");
            }
            else
                System.out.println("Invalid Information, Select Again!");
        }
    }
    
    /*
     * Imprime el tablero por la salida estandar en formato 2D 
     */
    private void print_table () {
        
        for(int i = 0; i<4; i++) {
            for(int k = 0; k<4; k++) {
                for(int j = 0; j<4; j++) {
                    System.out.print(this.table[k][i][j]+" ");
                }
                System.out.print("  ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
    /*
     * Evalua la funcion de evaluacion para min y max; y busca minimizar su valor
     * para beneficiar a la ai
     */
    private void evaluate () {
        
        int t = 0;
        int f = 0;
        int c = 0;
        int diff = 0;
        
        for(int k = 0; k<4; k++) {
            for(int i = 0; i<4; i++) {
                for(int j = 0; j<4; j++) {
                    if(this.table[k][i][j] == '-') {
                        this.table[k][i][j] = this.ai;
                        int max = evaluate_max();
                        int min = evaluate_min();
                        int new_diff = max - min;
                        if (new_diff < diff || diff == 0) {
                            diff = new_diff;
                            t = k;
                            f = i;
                            c = j;
                        }
                        this.table[k][i][j] = '-';
                    }
                }
            }
        }
        this.table[t][f][c] = this.ai;
    } 
    
    /*
     * Evalua todos los casos de la funcion de evaluacion para el jugador(human)
     */
    private int evaluate_max () {
    
        this.actualPlayer  = this.human;
        this.againstPlayer = this.ai;
        return evaluate_case();
 
    }
    
    /*
     * Evalua todos los casos de la funcion de evaluacion para la maquina(ai)
     */
    private int evaluate_min () {
        
        this.actualPlayer  = this.ai;
        this.againstPlayer = this.human;
        return evaluate_case();
    }
    
    /*
     * Evalua todos los casos para TICTACTOE_3D
     */
    private int evaluate_case() {
        int value = CHECK_HORIZONTALLY() +
                    CHECK_VERTICALLY() +
                    CHECK_DIAGONALLY_LR() +
                    CHECK_DIAGONALLY_RL() +
                    CHECK_ACROSS_SAME_POSITION() +
                    CHECK_ACROSS_HORIZONTALLY_LR() +
                    CHECK_ACROSS_HORIZONTALLY_RL() +
                    CHECK_ACROSS_VERTICALLY_UD() + 
                    CHECK_ACROSS_VERTICALLY_DU() + 
                    CHECK_ACROSS_DIAGONALLY_LR_UD() +
                    CHECK_ACROSS_DIAGONALLY_LR_DU() +
                    CHECK_ACROSS_DIAGONALLY_RL_UD() + 
                    CHECK_ACROSS_DIAGONALLY_RL_DU();
        return value;
    }
    
    /*
     * Da inicio al juego TICTACTOE_3D
     */
    public void play () throws IOException {
        
       BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
       select_piece();
       select_first_play();
       initialize_game();
       
       for(int i = 0; i<32; i++) {
           boolean b = true;
           if (this.begins) {
                evaluate();
                print_table();
           }
           while(b) {
                System.out.println("What Do You Want To Do? 1. Select Position 2.Exit");
                int c = Integer.parseInt(read.readLine());
                if (c == 1) {
                    select_position();
                    b = false;
                }
                else if (c == 2)
                    System.exit(0);
                else
                    System.out.println("Invalid Option");
           }
           print_table();  
           if (!this.begins) {
                evaluate();
                print_table();
           }
       }     
    }
    
    /*
     * Devuelve 1 pto. por una jugada posible. 10 ptos. por tener dos casillas 
     * marcadas sobre una jugada posible y 100 ptos. por tener tres casillas 
     * marcadas sobre una jugadas posible. Para lo anterior se entiende como
     * jugada posible donde no tenga casillas marcadas con piezas enemigas.
     * Si se corta una jugada posible donde hayan dos casillas con piezas 
     * contrarias se gana 10 ptos. Si se corta una jugada posible donde hayan
     * tres casillas con piezas contrarias se gana 100 ptos.
     */
    private int optimization (int w, int l, String s) {
        
        int c = 0;
        if (w == 4)
            finish(s);

        if (l == 0) {
            if (w == 2)
                return 10;
            if (w == 3)
                return 100;
        }
        if (w == 1) {
            if (l == 2)
                return 10;
            if (l == 3)
                return 100;
        }
        if (l == 0)
            c = 1;
        return c;
        
    }
    
    /*
     * Finaliza el juego, mostrando el ganador de la partida
     */
    private void finish (String s) {
        print_table ();
        System.out.println("The "+this.actualPlayer+" Player Won the Game, by "+s+"!");
        System.exit(0);
    }
    
    /*
     * Cheque las posibles jugadas horizontales
     */
    private int CHECK_HORIZONTALLY () {
        
        int w = 0;
        int l = 0;
        int c = 0;
            
        for(int k = 0; k<4; k++) {
            for(int i = 0; i<4; i++) {    
                for(int j = 0; j<4 ; j++) {   
                    if(this.table[k][i][j] == this.actualPlayer)
                        w++;
                    if(this.table[k][i][j] == this.againstPlayer) {
                        l++;
                    }   
                }
                c = c + optimization(w, l, "HORIZONTALLY");
                w = 0;
                l = 0;
            }
        }
        return c;
    }
    
    /*
     * Cheque las posibles jugadas verticales
     */
    private int CHECK_VERTICALLY () {
        
        int w = 0;
        int l = 0;
        int c = 0;
            
        for(int k = 0; k<4; k++) {
            for(int j = 0; j<4; j++) {    
                for(int i = 0; i<4 ; i++) {   
                    if(this.table[k][i][j] == this.actualPlayer)
                        w++;
                    if(this.table[k][i][j] == this.againstPlayer) {
                        l++;
                    }   
                }
                c = c + optimization(w, l, "VERTICALLY");
                w = 0;
                l = 0;
            }
        }
        return c;
    }
    
    /*
     * Cheque las posibles jugadas que sean diagonal sencilla de izquierda a
     * derecha
     */
    private int CHECK_DIAGONALLY_LR () {
        
        int w = 0;
        int l = 0;
        int c = 0;
            
        for(int k = 0; k<4; k++) {
            for(int i = 0; i<4; i++) {
                if(this.table[k][i][i] == this.actualPlayer)
                    w++;
                if(this.table[k][i][i] == this.againstPlayer) {
                    l++;
                }   
            }
            c = c + optimization(w, l, "DIAGONALLY_LR");
            w = 0;
            l = 0;
        }
        return c;
    }
    
    /*
     * Chequea las posibles jugadas que sean diagonal sencilla de derecha a
     * izquierda
     */
    private int CHECK_DIAGONALLY_RL () {
        
        int w = 0;
        int l = 0;
        int c = 0;
            
        for(int k = 0; k<4; k++) {
            for(int i = 0; i<4; i++) {
                if(this.table[k][i][3-i] == this.actualPlayer)
                    w++;
                if(this.table[k][i][3-i] == this.againstPlayer) {
                    l++;
                }   
            }
            c = c + optimization(w, l, "CHECK_DIAGONALLY_RL");
            w = 0;
            l = 0;
        }
        return c;
    }
    
    /*
     * Cheque las jugadas posibles que sean por la misma posicion entre los 
     * cuatro tableros
     */
    private int CHECK_ACROSS_SAME_POSITION () {
        
        int w = 0;
        int l = 0;
        int c = 0;
        
        for(int i = 0; i<4; i++) {
            for(int j = 0; j<4; j++) {
                for(int k = 0; k<4; k++) {
                    if(this.table[k][i][j] == this.actualPlayer)
                        w++;
                    if(this.table[k][i][j] == this.againstPlayer) {
                        l++;
                    }      
                }            
                c = c + optimization(w, l, "ACROSS_SAME_POSITION");
                w = 0;
                l = 0; 
            }   
        }
        return c; 
    }
    
    /*
     * Chequea las posibles jugadas que sean horizontal de izquierda a derecha
     * entre los cuatro tableros
     */
    private int CHECK_ACROSS_HORIZONTALLY_LR () {
        
        int w = 0;
        int l = 0;
        int c = 0;
        
        for(int i = 0; i<4; i++) {
            for(int k = 0; k<4; k++) {
                if(this.table[k][i][k] == this.actualPlayer)
                    w++;
                if(this.table[k][i][k] == this.againstPlayer) {
                    l++;
                }   
            }
            c = c + optimization(w, l, "CHECK_ACROSS_HORIZONTALLY_LR");
            w = 0;
            l = 0;      
        }
        return c;
    }
    
    /*
     * Chequea las posibles jugadas que sean horizontal de derecha a izquierda
     * entre los cuatro tableros
     */
    private int CHECK_ACROSS_HORIZONTALLY_RL () {
        
        int w = 0;
        int l = 0;
        int c = 0;
        
        for(int i = 0; i<4; i++) {
            for(int k = 0; k<4; k++) {
                if(this.table[k][i][3-k] == this.actualPlayer)
                    w++;
                if(this.table[k][i][3-k] == this.againstPlayer) {
                    l++;
                }   
            }
            c = c + optimization(w, l, "ACROSS_HORIZONTALLY_RL");
            w = 0;
            l = 0;      
        }
        return c;  
    }
    
    /*
     * Chequea las posibles jugadas que sean vertical de arriba hacia abajo
     * entre los cuatro tableros
     */
    private int CHECK_ACROSS_VERTICALLY_UD () {
        
        int w = 0;
        int l = 0;
        int c = 0;
        
        for(int j = 0; j<4; j++) {
            for(int k = 0; k<4; k++) {
                if(this.table[k][k][j] == this.actualPlayer)
                    w++;
                if(this.table[k][k][j] == this.againstPlayer) {
                    l++;
                }   
            }
            c = c + optimization(w, l, "ACROSS_VERTICALLY_UD");
            w = 0;
            l = 0;      
        }
        return c; 
    }
    
    /*     
     * Chequea las posibles jugadas que sean vertical de abajo hacia arriba
     * entre los cuatro tableros
     */
    private int CHECK_ACROSS_VERTICALLY_DU () {
        
        int w = 0;
        int l = 0;
        int c = 0;
        
        for(int j = 0; j<4; j++) {
            for(int k = 0; k<4; k++) {
                if(this.table[k][3-k][j] == this.actualPlayer)
                    w++;
                if(this.table[k][3-k][j] == this.againstPlayer) {
                    l++;
                }   
            }
            c = c + optimization(w, l, "ACROSS_VERTICALLY_DU");
            w = 0;
            l = 0;      
        }
        return c;   
    }
    
    /*
     * Chequea las posibles jugadas que sean diagonal de izquierda a derecha y 
     * de arriba hacia abajo entre los cuatro tableros
     */
    private int CHECK_ACROSS_DIAGONALLY_LR_UD () {
        
        int w = 0;
        int l = 0;
        int c = 0;
        
        for(int k = 0; k<4; k++) {
            if(this.table[k][k][k] == this.actualPlayer)
                w++;
            if(this.table[k][k][k] == this.againstPlayer) {
                l++;
            }   
        }
        c = c + optimization(w, l, "ACROSS_DIAGONALLY_LR_UD");
        return c;
    }
    
    /*
     * Chequea las posibles jugadas que sean diagonal de derecha a izquierda y 
     * de abajo hacia arriba entre los cuatro tableros
     */
    private int CHECK_ACROSS_DIAGONALLY_LR_DU () {
        
        int w = 0;
        int l = 0;
        int c = 0;
        
        for(int k = 0; k<4; k++) {
            if(this.table[k][3-k][k] == this.actualPlayer)
                w++;
            if(this.table[k][3-k][k] == this.againstPlayer) {
                l++;
            }   
        }
        c = c + optimization(w, l, "ACROSS_DIAGONALLY_LR_DU");
        return c;
    }
    
    /*
     * Chequea las posibles jugadas que sean diagonal de derecha a izquierda y 
     * de arriba hacia abajo entre los cuatro tableros
     */
    private int CHECK_ACROSS_DIAGONALLY_RL_UD () {
        
        int w = 0;
        int l = 0;
        int c = 0;
        
        for(int k = 0; k<4; k++) {
            if(this.table[k][k][3-k] == this.actualPlayer)
                w++;
            if(this.table[k][k][3-k] == this.againstPlayer) {
                l++;
            }   
        }
        c = c + optimization(w, l, "ACROSS_DIAGONALLY_RL_UD");
        return c;
    }
    
    /*
     * Chequea las posibles jugadas que sean diagonal de izquierda a derecha y 
     * de abajo hacia arriba entre los cuatro tableros
     */
    private int CHECK_ACROSS_DIAGONALLY_RL_DU () {
        
        int w = 0;
        int l = 0;
        int c = 0;
        
        for(int k = 0; k<4; k++) {
            if(this.table[k][3-k][3-k] == this.actualPlayer)
                w++;
            if(this.table[k][3-k][3-k] == this.againstPlayer) {
                l++;
            }   
        }
        c = c + optimization(w, l, "ACROSS_DIAGONALLY_RL_DU");
        return c;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        TICTACTOE_3D game = new TICTACTOE_3D();
        game.play();
    }
}