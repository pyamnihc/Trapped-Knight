import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.jfree.graphics2d.svg.*;
import java.awt.Color;
import java.awt.BasicStroke;
import org.jfree.graphics2d.svg.ViewBox;
import java.awt.Font;

import java.io.File;

import java.util.NoSuchElementException;
import java.io.IOException;

import boards.*;
import chessmen.*;

public class StartSkips {
    static final int WIDTH = 10000;
    static final int HEIGHT = 10000;
    
    private Board game;
    private Piece chessman;
    private int start;
    private int skips;
    private int motion[][];
    private int nextpos[][];
    private int currentpos[];
    private int prevpos[];

    private int maxdim = 0;
    final int scale = 1;
    final int fontsize = 1;
    final float linewidth = (float)0.2;

    private static int maxweight;
    private Set<Integer> nextweights;
    private Set<Integer> visited;
    private List<Integer> weights;

    private SVGGraphics2D g;

    private int MAX_ITER = 100000; 
    private float hue = 0;
    private float inc;

    public StartSkips (Board game, Piece chessman, int start, int skips) {
        this.game = game;
        this.chessman = chessman;
        this.start = start;
        this.skips = skips;
        this.motion = this.chessman.move();
        this.nextpos = new int[motion.length][2];
        
        this.visited = new HashSet<Integer> ();
        this.weights = new ArrayList<Integer> ();

        this.g = new SVGGraphics2D(WIDTH, HEIGHT);
        this.g.setBackground(Color.BLACK);
        this.g.clearRect(0, 0, WIDTH, HEIGHT);
        this.g.setFont(new Font("TimesRoman", Font.PLAIN, fontsize));
        this.g.setStroke(new BasicStroke(linewidth));

        this.inc = (float)1/skips;
    }

    public static void main(String args[]) {
        try {
            Board game = (Board)(Class.forName("boards." + args[0]).newInstance());
            
            String p_args[] = args[1].split(",");
            Class<?> p = Class.forName("chessmen." + p_args[0]);
            Piece chessman;
            switch (p_args.length) {
                case 1: chessman = (Piece)p.newInstance();
                        break;
                case 2: chessman = (Piece)p.getConstructor(int.class).newInstance(Integer.parseInt(p_args[1]));
                        break;
                case 3: chessman = (Piece)p.getConstructor(int.class, int.class).newInstance(Integer.parseInt(p_args[1]), Integer.parseInt(p_args[2]));
                        break;
                default: throw new Exception();
            }
            int start = Integer.parseInt(args[2]);
            if (start <= 0) throw new Exception();

            int skips = Integer.parseInt(args[3]);
            StartSkips SS = new StartSkips(game, chessman, start, skips);
            SS.bktk_run(start, 1);
        }
        catch (Exception e) {
            System.out.println("Know your men & boards.");
            System.out.println("Run as: java Trapped BoardName PieceName,steps Start Skips");
            System.out.println("BoardName := {Spiral, Diagonal, Snake, SquareTriangle}");
            System.out.println("PieceName := {Knight, Rook, Bishop, Queen}");
            System.out.println("steps := Integer number of steps per move, comma separated pair for Knight");
            System.out.println("Start := Positive integer number of starting weight");
            System.out.println("Skips := Integer number of skips");
            e.printStackTrace();
        }
    }
    
    public void bktk_run (int currentweight, int scount) {
        currentpos = game.weight2pos(currentweight);
        prevpos = currentpos.clone();

        maxweight = currentweight;
        visited.add(currentweight);

        int iter = 0;

        while(true) {
            iter++;
            
            for (int i = 0; i < motion.length; i++) {
                nextpos[i][0] = currentpos[0] + motion[i][0];
                nextpos[i][1] = currentpos[1] + motion[i][1];
            }

            nextweights = game.pos2weights(nextpos);   
            nextweights.removeAll(visited);

            try {
                currentweight = Collections.min(nextweights);
                visited.add(currentweight);
                weights.add(currentweight);
                if (iter == MAX_ITER) throw new NoSuchElementException();
            }
            catch (NoSuchElementException e) {
                if (iter == MAX_ITER) 
                    System.out.printf("Terminated ! after %d iterations on weight %d\n", iter, currentweight);
//                else
//                    System.out.printf("Trapped ! after %d iterations on weight %d\n", iter, currentweight);
                
                g.setPaint(new Color(Color.HSBtoRGB(hue, 1, 1)));
                g.fillOval(WIDTH/2 + scale*currentpos[0] - 2*scale, HEIGHT/2 - scale*currentpos[1] - 2*scale, 4*scale, 4*scale); 
                hue += inc;

                if (scount < skips) {
                    bktk_run (weights.get(weights.indexOf(currentweight) - 1), scount + 1);
                    return;
                }
                else { 
                    try {
                        File file = new File("StartSkips_" + start + "_" + motion[0][0] + "_" + motion[0][1] + ".svg");
                        ViewBox bound = new ViewBox(WIDTH/2 - scale*maxdim, HEIGHT/2 - scale*maxdim, scale*2*maxdim, scale*2*maxdim);
                        SVGUtils.writeToSVG(file, g.getSVGElement(null, false, bound, null, null));
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    return;
                }
            }

            try {
                prevpos[0] = currentpos[0]; 
                prevpos[1] = currentpos[1]; 

                currentpos = game.weight2pos(currentweight);

                if (currentweight > maxweight) {
                    maxweight = currentweight;
                    maxdim = Math.abs(currentpos[0]) > Math.abs(currentpos[1]) ? currentpos[0] : currentpos[1];
                    maxdim = Math.abs(maxdim);
                } 
                
//                g.drawLine(WIDTH/2 + scale*prevpos[0], HEIGHT/2 - scale*prevpos[1], 
//                        WIDTH/2 + scale*currentpos[0], HEIGHT/2 - scale*currentpos[1]);
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Wrecked !");
            }
        }
    }
}
