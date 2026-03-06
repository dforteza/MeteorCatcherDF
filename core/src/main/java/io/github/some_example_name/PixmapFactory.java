package io.github.some_example_name;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

// Genera todas las texturas del juego de forma procedural con Pixmap.
public final class PixmapFactory {

    private PixmapFactory() {}

    public static Texture createBackground(int w, int h) {
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);

        // Gradiente vertical: azul muy oscuro → azul-púrpura suave
        for (int y = 0; y < h; y++) {
            float t = (float) y / h;
            pm.setColor(0.04f + 0.02f * t, 0.01f + 0.03f * t, 0.14f + 0.07f * t, 1f);
            for (int x = 0; x < w; x++) pm.drawPixel(x, y);
        }

        // Estrellas con semilla fija (patrón reproducible)
        Random rng = new Random(42L);
        for (int i = 0; i < 300; i++) {
            int sx = rng.nextInt(w);
            int sy = rng.nextInt(h);
            float b = 0.35f + rng.nextFloat() * 0.65f;
            pm.setColor(b, b, b, 1f);
            pm.drawPixel(sx, sy);
            // Algunas estrellas tienen 2×2 píxeles para parecer más brillantes
            if (rng.nextFloat() < 0.12f) {
                pm.drawPixel(Math.min(sx + 1, w - 1), sy);
                pm.drawPixel(sx, Math.min(sy + 1, h - 1));
            }
        }

        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    public static Texture createShip(int w, int h) {
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(0, 0, 0, 0);
        pm.fill();

        // ── Cuerpo principal (elipse plana y ancha) ───────────────────────────
        int bodyY  = h * 48 / 70;
        int bodyRx = w / 2 - 4;
        int bodyRy = h / 7;
        pm.setColor(0.0f, 0.42f, 0.72f, 1f);     // borde oscuro (dibujado primero)
        fillEllipseScanline(pm, w / 2, bodyY, bodyRx, bodyRy);
        pm.setColor(0.18f, 0.72f, 0.92f, 1f);    // relleno cian (1 px más pequeño)
        fillEllipseScanline(pm, w / 2, bodyY, bodyRx - 1, bodyRy - 1);

        // ── Cúpula / cabina ───────────────────────────────────────────────────
        int domeY  = h * 24 / 70;
        int domeRx = w / 5;
        int domeRy = h / 5;
        pm.setColor(0.10f, 0.55f, 0.82f, 1f);    // borde cúpula
        fillEllipseScanline(pm, w / 2, domeY, domeRx, domeRy);
        pm.setColor(0.55f, 0.92f, 1.0f, 1f);     // relleno cúpula
        fillEllipseScanline(pm, w / 2, domeY, domeRx - 1, domeRy - 1);

        // ── Luces amarillas (3 puntos a lo largo del cuerpo) ─────────────────
        pm.setColor(1f, 0.95f, 0.0f, 1f);
        pm.fillCircle(w * 16 / 80, bodyY, 3);
        pm.fillCircle(w / 2,       bodyY + 2, 3);
        pm.fillCircle(w * 64 / 80, bodyY, 3);

        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    // Rellena una elipse con scanlines horizontales (libGDX no expone fillEllipse).
    private static void fillEllipseScanline(Pixmap pm, int cx, int cy, int rx, int ry) {
        if (rx <= 0 || ry <= 0) return;
        for (int dy = -ry; dy <= ry; dy++) {
            double t = (double) dy / ry;
            int xSpan = (int) (rx * Math.sqrt(Math.max(0.0, 1.0 - t * t)));
            pm.drawLine(cx - xSpan, cy + dy, cx + xSpan, cy + dy);
        }
    }

    public static Texture createDrop(int size) {
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pm.setColor(0, 0, 0, 0);
        pm.fill();
        pm.setColor(1f, 1f, 1f, 1f);
        pm.fillCircle(size / 2, size / 2, size / 2 - 1);
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    public static Texture createLogo(int size) {
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pm.setColor(0, 0, 0, 0);
        pm.fill();

        // Anillo exterior azul-cian
        pm.setColor(0.18f, 0.72f, 0.92f, 1f);
        pm.fillCircle(size / 2, size / 2, size / 2 - 2);

        // Capa interior más clara
        pm.setColor(0.55f, 0.92f, 1.0f, 1f);
        pm.fillCircle(size / 2, size / 2, size / 2 - 12);

        // Brillo especular (highlight)
        pm.setColor(1f, 1f, 1f, 0.85f);
        pm.fillCircle(size / 2 - size / 5, size / 2 - size / 5, size / 7);

        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }
}
