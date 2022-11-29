package principal;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

public class menuPopCotacao extends MouseAdapter {
    JPopupMenu popup;
    private static Point posicao;

    
    public menuPopCotacao(JPopupMenu p) {
        popup = p;
    }
    
    private void checkPopup(MouseEvent e) {
        if(e.isPopupTrigger()) {
            posicao = e.getPoint();
            popup.show(e.getComponent(), e.getX(), e.getY());
        }                
    }
    
    public static Point getPosicao() {
        return posicao;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        checkPopup(e);
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        checkPopup(e);
    }
    
}
