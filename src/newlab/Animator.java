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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

public class Animator implements Runnable {

    Graphics g;
    Cell[][] cells;
    int heroI;
    int heroJ;
    //Cell end;
    int heroX;
    int heroY;
    int dx = 5;
    int dy = 5;
    int heightOfCell = 60;
    int widthOfCell = 60;
    int wallCount;
    int grassCount;
    int matrixHeight;
    int matrixWidth;
    int levelCount = 3;
    BufferedImage grayGrass;
    BufferedImage colorGrass;
    BufferedImage grayWall;
    BufferedImage colorWall;
    BufferedImage grayMasha;
    BufferedImage colorMasha;
    BufferedImage miha;
    BufferedImage leftMiha;
    BufferedImage rightMiha;
    BufferedImage anfasMiha;
    BufferedImage happyEnd;
    BufferedImage buffer;
    Graphics bufG;

    public Animator(Graphics g) {
        this.g = g;
        buffer = new BufferedImage(720, 720, BufferedImage.TYPE_INT_RGB);
        bufG = buffer.getGraphics();
        bufG.setColor(Color.WHITE);
        bufG.fillRect(0, 0, 720, 720);
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
            grayMasha = ImageIO.read(new File("images\\gray_maria.png"));
            colorMasha = ImageIO.read(new File("images\\color_maria.png"));
            anfasMiha = ImageIO.read(new File("images\\bear.png"));
            leftMiha = ImageIO.read(new File("images\\left.jpg"));
            rightMiha = ImageIO.read(new File("images\\right.jpg"));
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
        Cell start = cellList.get(matrixHeight * matrixWidth - 1);

        Collections.shuffle(cellList);
        for (int i = 0; i < matrixHeight; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                cells[i][j] = cellList.get(i * matrixHeight + j);
                cells[i][j].setI(i);
                cells[i][j].setJ(j);
            }
        }
        heroI = start.getI();
        heroJ = start.getJ();
        setVisibleAroundMiha();

    }

    private void loadFromFile(String path) {
        try {
            FileReader FR = new FileReader(path);
            BufferedReader BR = new BufferedReader(FR);
            ArrayList<String> strings = new ArrayList();
            String S = "";
            while ((S = BR.readLine()) != null) {
                strings.add(S);
            }
            matrixHeight = strings.size();
            matrixWidth = strings.get(0).length();
            cells = new Cell[matrixHeight][matrixWidth];
            for (int i = 0; i < matrixHeight; i++) {
                String types = strings.get(i);
                for (int j = 0; j < matrixWidth; j++) {
                    cells[i][j] = new Cell();

                    char type = types.charAt(j);//получение символа из массива строки по индексу
                    switch (type) {
                        case '0':
                            cells[i][j].setType(Type.GRASS);
                            break;
                        case '1':
                            cells[i][j].setType(Type.WALL);
                            break;
                        case 'M':
                            cells[i][j].setType(Type.MASHA);
                            break;
                        case 'H':
                            cells[i][j].setType(Type.GRASS);
                            heroI = i;
                            heroJ = j;
                            break;
                    }
                }
            }
            heroY = heroI * heightOfCell;
            heroX = heroJ * widthOfCell;
            miha=anfasMiha;
            setVisibleAroundMiha();
            Draw();
        } catch (Exception e) {

        }
    }

    private void setVisibleAroundMiha() {
        for (int i = heroI - 1; i <= heroI + 1; i++) {
            for (int j = heroJ - 1; j <= heroJ + 1; j++) {
                if (i >= 0 && j >= 0 && i < matrixHeight && j < matrixWidth) {
                    cells[i][j].setVisible(true);
                }
            }
        }
    }

    private void init() {
        loadImages();

    }

    void Draw() {
        bufG.setColor(Color.BLACK);
        for (int i = 0; i < matrixHeight; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                int x = j * widthOfCell;
                int y = i * heightOfCell;
                if (cells[i][j].getVisible() == true) {
                    if ((Math.abs(i - heroI) <= 1) && (Math.abs(j - heroJ) <= 1)) {
                        switch (cells[i][j].getType()) {
                            case WALL:
                                bufG.drawImage(colorWall, x, y, widthOfCell, heightOfCell, null);
                                break;
                            case GRASS:
                                bufG.drawImage(colorGrass, x, y, widthOfCell, heightOfCell, null);
                                break;
                            case MASHA:
                                bufG.drawImage(colorGrass, x, y, widthOfCell, heightOfCell, null);
                                bufG.drawImage(colorMasha, x, y, widthOfCell, heightOfCell, null);
                                break;
                        }
                    } else {
                        switch (cells[i][j].getType()) {
                            case WALL:
                                bufG.drawImage(grayWall, x, y, widthOfCell, heightOfCell, null);
                                break;
                            case GRASS:
                                bufG.drawImage(grayGrass, x, y, widthOfCell, heightOfCell, null);
                                break;
                            case MASHA:
                                bufG.drawImage(grayGrass, x, y, widthOfCell, heightOfCell, null);
                                bufG.drawImage(grayMasha, x, y, widthOfCell, heightOfCell, null);
                                break;
                        }

                    }
                } else {
                    bufG.fillRect(x, y, widthOfCell, heightOfCell);

                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            }
        }
        bufG.drawImage(miha, heroX, heroY, widthOfCell, heightOfCell, null);
    }

    boolean isWin() {
        if (cells[heroI][heroJ].getType() == Type.MASHA) {
            return true;
        } else {
            return false;
        }
    }

    private void move(int di, int dj) {
        int i = heroI + di;
        int j = heroJ + dj;
        if (i >= 0 && j >= 0 && i < matrixHeight && j < matrixWidth) {
            if (cells[i][j].getType() != Type.WALL) {
                heroI = i;
                heroJ = j;
                setVisibleAroundMiha();
            }
        }
        Draw();
    }

    public void left() {
        //miha = leftMiha;
        move(0, -1);
    }

    public void right() {
        //miha=rightMiha;
        move(0, 1);
    }

    public void up() {
        //miha=anfasMiha;
        move(-1, 0);
    }

    public void down() {
        //miha=anfasMiha;
        move(1, 0);
    }

    @Override
    public void run() {
        Font f = new Font("Verdana", Font.PLAIN, 30);
        g.setFont(f);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1200, 1200);
        for (int level = 1; level <= levelCount; level++) {
            loadFromFile("maps\\map" + level + ".txt");
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 720, 720);
            g.setColor(Color.BLACK);
            g.drawString("Уровень" + level, 100, 320);
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            while (isWin() == false) {
                g.drawImage(buffer, 0, 0, 720, 720, null);
                if (heroX != heroJ * widthOfCell) {

                    if (heroX < heroJ * widthOfCell) {
                        heroX = heroX + dx;

                    } else {
                        heroX = heroX - dx;
                    }
                    Draw();
                }
                if (heroY != heroI * heightOfCell) {

                    if (heroY < heroI * heightOfCell) {
                        heroY = heroY + dy;

                    } else {
                        heroY = heroY - dy;
                    }
                    Draw();
                }

                try {
                    Thread.sleep(25);
                } catch (Exception e) {
                }
            }
            g.drawImage(happyEnd, 0, 0, 720, 720, null);
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
        }
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 720, 720);
        g.setColor(Color.BLACK);
        g.drawString("Игра окончена!", 100, 320);

    }

}
