import java.util.Set;
import java.util.HashSet;
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

public class Trapped {
    static final int WIDTH = 10000;
    static final int HEIGHT = 10000;

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
            run(game, chessman);
        }
        catch (Exception e) {
            System.out.println("Know your men & boards.");
            System.out.println("Run as: java Trapped BoardName PieceName,steps");
            System.out.println("BoardName := {Spiral, Diagonal, Snake, SquareTriangle}");
            System.out.println("PieceName := {Knight, Rook, Bishop, Queen}");
            System.out.println("steps := Integer number of steps per move, comma separated pair for Knight");
            e.printStackTrace();
        }
    }
    
    public static void run (Board game, Piece chessman) {
        int motion[][] = chessman.move();
        int nextpos[][] = new int[motion.length][2];
        int currentpos[] = new int [2];
        int prevpos[] = new int [2];
        int maxdim = 0;
        final int scale = 4;
        final int fontsize = 1;
        final float linewidth = (float)0.2;

        int currentweight = 1;
        int maxweight = currentweight;
        Set<Integer> nextweights = new HashSet<Integer>();
        Set<Integer> visited = new HashSet<Integer>();
        visited.add(currentweight);
        
        SVGGraphics2D g = new SVGGraphics2D(WIDTH, HEIGHT);
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setStroke(new BasicStroke((float)0.5));
        g.setPaint(new Color(255, 255, 255, 127));
        g.drawLine(WIDTH/2 + currentpos[0] - 1, HEIGHT/2 - currentpos[1] - 1, 
                WIDTH/2 + currentpos[0] + 1, HEIGHT/2 - currentpos[1] + 1);
        g.drawLine(WIDTH/2 + currentpos[0] + 1, HEIGHT/2 - currentpos[1] - 1,
                WIDTH/2 + currentpos[0] - 1, HEIGHT/2 - currentpos[1] + 1);

        g.setFont(new Font("TimesRoman", Font.PLAIN, fontsize));
        g.setStroke(new BasicStroke(linewidth));
       
        int MAX_ITER = 100000; 
        int iter = 0;
        float hue = 0;
        float inc = (float)10/MAX_ITER;

        while(true) {
            iter++;
            
//            g.setPaint(Color.WHITE);
//            g.drawString(Integer.toString(currentweight), (float)(WIDTH/2 + scale*currentpos[0] - fontsize/2.),
//                    (float)(HEIGHT/2 - scale*currentpos[1] + fontsize/2.));
            
            for (int i = 0; i < motion.length; i++) {
                nextpos[i][0] = currentpos[0] + motion[i][0];
                nextpos[i][1] = currentpos[1] + motion[i][1];
            }
            nextweights = game.pos2weights(nextpos);   
            nextweights.removeAll(visited);

            try {
                currentweight = Collections.min(nextweights);
                visited.add(currentweight);
                if (iter == MAX_ITER) throw new NoSuchElementException();
            }
            catch (NoSuchElementException e) {
                if (iter == MAX_ITER) 
                    System.out.printf("Terminated ! after %d iterations on weight %d\n", iter, currentweight);
                else
                    System.out.printf("Trapped ! after %d iterations on weight %d\n", iter, currentweight);
               
                try {
                    File file = new File("Trap_" + motion[0][0] + "_" + motion[0][1] + ".svg");
                    
                    g.setStroke(new BasicStroke((float)0.5));
                    g.setPaint(new Color(255, 255, 255, 127));
                    g.drawLine(WIDTH/2 + scale*currentpos[0] - 1, HEIGHT/2 - scale*currentpos[1] - 1 , 
                            WIDTH/2 + scale*currentpos[0] + 1, HEIGHT/2 - scale*currentpos[1] + 1);
                    g.drawLine(WIDTH/2 + scale*currentpos[0] + 1, HEIGHT/2 - scale*currentpos[1] - 1, 
                            WIDTH/2 + scale*currentpos[0] - 1, HEIGHT/2 - scale*currentpos[1] + 1);
 
                    ViewBox bound = new ViewBox(WIDTH/2 - scale*maxdim, HEIGHT/2 - scale*maxdim, scale*2*maxdim, scale*2*maxdim);
                    SVGUtils.writeToSVG(file, g.getSVGElement(null, false, bound, null, null));
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }

                return;
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
                
                g.setPaint(new Color(Color.HSBtoRGB(hue, 1, 1)));
                g.drawLine(WIDTH/2 + scale*prevpos[0], HEIGHT/2 - scale*prevpos[1], 
                        WIDTH/2 + scale*currentpos[0], HEIGHT/2 - scale*currentpos[1]);
                hue += inc;
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Wrecked !");
            }
        }
    }
}
