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
import java.awt.Color;
import java.awt.Graphics;
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

    public Animator(Graphics g) {
        this.g = g;
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
        setVisibleAroundMiha();

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
        generate();
    }

    void Draw() {
        g.setColor(Color.BLACK);
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int x = j * widthOfCell;
                int y = i * heightOfCell;
                if (i == heroI && j == heroJ) {
                    g.drawImage(miha, x, y, widthOfCell, heightOfCell, null);
                } else if (cells[i][j].getVisible() == true) {
                    if ((Math.abs(i - heroI) <= 1) && (Math.abs(j - heroJ) <= 1)) {
                        switch (cells[i][j].getType()) {
                            case WALL:
                                g.drawImage(colorWall, x, y, widthOfCell, heightOfCell, null);
                                break;
                            case GRASS:
                                g.drawImage(colorGrass, x, y, widthOfCell, heightOfCell, null);
                                break;
                            case MASHA:
                                g.drawImage(colorMasha, x, y, widthOfCell, heightOfCell, null);
                                break;
                        }
                    } else {
                        switch (cells[i][j].getType()) {
                            case WALL:
                                g.drawImage(grayWall, x, y, widthOfCell, heightOfCell, null);
                                break;
                            case GRASS:
                                g.drawImage(grayGrass, x, y, widthOfCell, heightOfCell, null);
                                break;
                            case MASHA:
                                g.drawImage(grayMasha, x, y, widthOfCell, heightOfCell, null);
                                break;
                        }

                    }
                } else {
                    g.fillRect(x, y, widthOfCell, heightOfCell);

                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            }
        }
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
        while(isWin()== false){
            Draw();

            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
        g.drawImage(happyEnd, 0, 0, matrixSize*widthOfCell,matrixSize*heightOfCell,null);
    }
}
