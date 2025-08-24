package cc.xypp.damage_number.api;

import cc.xypp.damage_number.api.decoration.INumberDecoration;
import cc.xypp.damage_number.data.DamageTextFmt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DamageEmitEvent extends Event {
    protected int color;
    protected float amount;
    protected float instant;
    protected int combo;
    protected DamageSource source;
    protected Player player;
    protected List<INumberDecoration> decorations;
    protected @NotNull DamageTextFmt textFormat;

    public DamageEmitEvent(float amount,
                           float instant,
                           int combo,
                           int color,
                           List<INumberDecoration> decorations,
                           @NotNull DamageTextFmt textFormat,
                           DamageSource source,
                           Player player
    ) {
        this.amount = amount;
        this.instant = instant;
        this.decorations = decorations;
        this.combo = combo;
        this.color = color;
        this.source = source;
        this.player = player;
        this.textFormat = textFormat;
    }


    public int getColor() {
        return color;
    }

    public float getAmount() {
        return amount;
    }

    public float getInstant() {
        return instant;
    }

    public int getCombo() {
        return combo;
    }

    public DamageSource getSource() {
        return source;
    }

    public Player getPlayer() {
        return player;
    }

    public List<INumberDecoration> getDecorations() {
        return decorations;
    }

    public @NotNull DamageTextFmt getFormat() {
        return textFormat;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setInstant(float instant) {
        this.instant = instant;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public void addDecoration(INumberDecoration decoration) {
        decorations.add(decoration);
    }
}
