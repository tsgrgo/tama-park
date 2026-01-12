import com.nttdocomo.ui.Canvas;
import com.nttdocomo.ui.Graphics;

public class a extends Canvas {
   public a(b var1) {
   }

   public void processEvent(int var1, int var2) {
      b.Code(var1, var2);
   }

   public void paint(Graphics var1) {
      if (b.Code == 2) {
         b.Code = 3;
         if (b.Exceptions) {
            var1.lock();
            b.I = b.StackMap;
            b.StackMap = false;
            if (b.Z) {
               b.Z = false;
            }

            var1.setFont(b.J);
            b.Exceptions(var1);
            var1.unlock(true);
         } else {
            b.Exceptions = true;
         }

         b.Code = 0;
      }

   }
}
