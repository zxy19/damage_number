package cc.xypp.damage_number.data;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class DamageTextFmt {
    public static final DamageTextFmt DEFAULT_TEXT_FMT = new DamageTextFmt("damage_list.content", Component.empty(), Component.empty());
    String formatKey;
    Component prepend;
    Component append;

    public DamageTextFmt(String formatKey, Component prepend, Component append) {
        this.formatKey = formatKey;
        this.prepend = prepend;
        this.append = append;
    }

    public static DamageTextFmt getDefault() {
        return new DamageTextFmt("damage_list.content", Component.empty(), Component.empty());
    }

    public static DamageTextFmt fromNetwork(FriendlyByteBuf buf) {
        return new DamageTextFmt(
                buf.readUtf(),
                buf.readComponent(),
                buf.readComponent()
        );
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeUtf(formatKey);
        buf.writeComponent(prepend);
        buf.writeComponent(append);
    }

    public void append(Component component) {
        append = append.copy().append(component);
    }

    public void prepend(Component component) {
        prepend = component.copy().append(prepend);
    }

    public void setFormatKey(String formatKey) {
        this.formatKey = formatKey;
    }

    public Component getFinal(String format) {
        return prepend.copy().append(Component.translatable(format, formatKey)).append(append);
    }
}
