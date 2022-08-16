package com.technovision.advancedgenetics.events;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Stores data to be passed to DamageReceivedEvent.
 *
 * @author TechnoVision
 */
public class DamageReceivedEvent {

    private final ServerPlayerEntity player;
    private final DamageSource source;
    private final float amount;

    public DamageReceivedEvent(ServerPlayerEntity player, DamageSource source, float amount) {
        this.player = player;
        this.source = source;
        this.amount = amount;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getAmount() {
        return amount;
    }

    /**
     * Event that fires every time a player receives damage on the server side.
     *
     * @param event the event details.
     * @return true if event is canceled and player takes no damage, otherwise false.
     */
    public static boolean onDamageReceivedEvent(DamageReceivedEvent event) {
        ServerPlayerEntity player = event.getPlayer();
        DamageSource source = event.getSource();
        PlayerGeneticsComponent component = player.getComponent(ComponentRegistry.PLAYER_GENETICS);

        // Handles "No Fall Damage" gene
        if (source.isFromFalling() && component.hasGene(Genes.NO_FALL_DAMAGE)) {
            return true;
        }
        // Handles "Poison Immunity" gene
        if (player.hasStatusEffect(StatusEffects.POISON) && component.hasGene(Genes.POISON_IMMUNITY)) {
            player.removeStatusEffect(StatusEffects.POISON);
            return true;
        }
        return false;
    }
}
