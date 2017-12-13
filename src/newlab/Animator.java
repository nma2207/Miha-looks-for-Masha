/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlab;

/**
 *
 * @author Пользователь
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Пользователь
 */
enum Type {
    GRASS, WALL, MASHA
}

class Cell {

    Type type;
    int i, j;
    boolean visible = false;

    public Type getType() {
        return type;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setType(Type value) {
        type = value;
    }

    public void setI(int value) {
        i = value;
    }

    public void setJ(int value) {
        j = value;
    }

    public void setVisible(boolean value) {
        visible = value;
    }

    public Cell(Type t, int i, int j, boolean vis) {
        this.type = t;
        this.i = i;
        this.j = j;
        this.visible = vis;
    }

    public Cell() {

    }

}

public class Animator implements Runnable{

    Graphics g;
    Cell[][] cells = new Cell[12][12];
    int heroI;
    int heroJ;

    int heroX;
    int heroY;
    int heroDx = 10;
    int heroDy=10;
    //Cell end;
    int heightOfCell = 60;
    int widthOfCell = 60;
    int wallCount = 44;
    int grassCount = 99;
    int matrixSize = 12;
    BufferedImage grayGrass;
    BufferedImage colorGrass;
    BufferedImage grayWall;
    BufferedImage colorWall;
    BufferedImage grayMasha;
    BufferedImage colorMasha;
    BufferedImage miha;
    BufferedImage happyEnd;

    BufferedImage buffer;
    Graphics bufferG;

    int level=1;
    int levelCount = 3;
    public Animator(Graphics g) {
        this.g =g;
        buffer = new BufferedImage(720, 720, BufferedImage.TYPE_INT_RGB);
        bufferG = buffer.getGraphics();
        init();
    }

    Animator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void loadImages() {
        try {
            grayGrass = ImageIO.read(new File("images\\gray_grass.jpg"));
            colorGrass = ImageIO.read(new File("images\\color_grass.jpg"));
            grayWall = ImageIO.read(new File("images\\gray_wall.jpg"));
            colorWall = ImageIO.read(new File("images\\color_wall.jpg"));
            grayMasha = ImageIO.read(new File("images\\gray_maria.jpg"));
            colorMasha = ImageIO.read(new File("images\\color_maria.jpg"));
            miha = ImageIO.read(new File("images\\bear.jpg"));
            happyEnd = ImageIO.read(new File("images\\happyEnd.jpg"));
        } catch (Exception e) {
        }
    }

    private void generate() {
        List<Cell> cellList = new ArrayList<Cell>();
        Cell c = new Cell();
        c.setType(Type.MASHA);
        cellList.add(c);
        for (int i = 0; i < wallCount; i++) {
            Cell cell = new Cell();
            cell.setType(Type.WALL);
            cellList.add(cell);
        }
        for (int i = 0; i < grassCount; i++) {
            Cell cell = new Cell();
            cell.setType(Type.GRASS);
            cellList.add(cell);
        }
        Cell start = cellList.get(matrixSize * matrixSize - 1);

        Collections.shuffle(cellList);
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                cells[i][j] = cellList.get(i * matrixSize + j);
                cells[i][j].setI(i);
                cells[i][j].setJ(j);
            }
        }
        heroI = start.getI();
        heroJ = start.getJ();
        heroX = heroJ*widthOfCell;
        heroY = heroI*heightOfCell;
        setVisibleAroundMiha();
        Draw();

    }

    private void setVisibleAroundMiha() {
        for (int i = heroI - 1; i <= heroI + 1; i++) {
            for (int j = heroJ - 1; j <= heroJ + 1; j++) {
                if (i >= 0 && j >= 0 && i < matrixSize && j < matrixSize) {
                    cells[i][j].setVisible(true);
                }
            }
        }
    }

    private void init() {
        loadImages();
        //generate();
    }

    void Draw() {
        bufferG.setColor(Color.WHITE);
        bufferG.fillRect(0,0,720,720);
        bufferG.setColor(Color.BLACK);
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int x = j * widthOfCell;
                int y = i * heightOfCell;
                if (cells[i][j].getVisible() == true) {
                    if ((Math.abs(i - heroI) <= 1) && (Math.abs(j - heroJ) <= 1)) {
                        switch (cells[i][j].getType()) {
                            case WALL:
                                bufferG.drawImage(colorWall, x, y, widthOfCell, heightOfCell, null);
                                break;
                            case GRASS:
                                bufferG.drawImage(colorGrass, x, y, widthOfCell, heightOfCell, null);
                                break;
                            case MASHA:
                                bufferG.drawImage(colorMasha, x, y, widthOfCell, heightOfCell, null);
                                break;
                        }
                    } else {
                        switch (cells[i][j].getType()) {
                            case WALL:
                                bufferG.drawImage(grayWall, x, y, widthOfCell, heightOfCell, null);
                                break;
                            case GRASS:
                                bufferG.drawImage(grayGrass, x, y, widthOfCell, heightOfCell, null);
                                break;
                            case MASHA:
                                bufferG.drawImage(grayMasha, x, y, widthOfCell, heightOfCell, null);
                                break;
                        }

                    }
                } else {
                    bufferG.fillRect(x, y, widthOfCell, heightOfCell);

                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            }
        }
        bufferG.drawImage(miha, heroX, heroY, widthOfCell, heightOfCell, null);
    }
    boolean isWin()
    {
        if(cells[heroI][heroJ].getType()== Type.MASHA)
            return true;
        else{
            return false;
        }
    }
    private void move(int di,int dj)
    {
        int i = heroI + di;
        int j = heroJ + dj;
         if (i >= 0 && j >= 0 && i < matrixSize && j < matrixSize)
         {
             if(cells[i][j].getType()!=Type.WALL){
                 heroI = i;
                 heroJ = j;
                 setVisibleAroundMiha();
                 Draw();
             }
         }
    }
    
    public void left(){
        move(0,-1);
    }
     public void right(){
        move(0,1);
    }
      public void up(){
        move(-1,0);
    }
       public void down(){
        move(1,0);
    }
       @Override
    public void run(){
           Font f = new Font("Verdana", Font.PLAIN, 30);
           g.setFont(f);
           g.setColor(Color.white);

           //
           wallCount = 25-1-20;
           grassCount = 20;
           matrixSize = 5;
           for(level=1;level<=levelCount;level++)
           {
               generate();
               g.setColor(Color.white);
               g.fillRect(0,0,1000,800);
               g.setColor(Color.BLACK);
               g.drawString("Уровень "+level, 400, 400);
               try {
                   Thread.sleep(2000);
               } catch (Exception e) {
               }
               while(isWin()== false){
                   g.drawImage(buffer,0,0,720,720,null);

                   try {
                       Thread.sleep(100);
                   } catch (Exception e) {
                   }
                   if(heroX!=heroJ*widthOfCell)
                   {
                       if (heroX < heroJ*widthOfCell)
                           heroX+=heroDx;
                       else
                           heroX-=heroDx;
                       Draw();
                   }
                   if(heroY!=heroI*heightOfCell)
                   {
                       if (heroY < heroI*heightOfCell)
                           heroY+=heroDy;
                       else
                           heroY-=heroDy;
                       Draw();
                   }
               }
               g.drawImage(happyEnd, 0, 0, 720,720,null);
               try {
                   Thread.sleep(2000);
               } catch (Exception e) {
               }
               matrixSize+=1;
               //grassCount+=1;
               wallCount+=1;
               grassCount=matrixSize*matrixSize-wallCount-1;
           }
           g.setColor(Color.WHITE);
           g.fillRect(0,0,1000,800);
           g.setColor(Color.BLACK);
           g.drawString("Игра окончега", 400, 400);


    }
}
