package net.impleri.playerskills.restrictions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractRestriction<Target> {
    private static final Predicate<Player> DEFAULT_CONDITION = (Player player) -> true;
    public final Target target;
    public final Target replacement;

    public final @NotNull Predicate<Player> condition;

    public final Set<ResourceLocation> includeDimensions;
    public final Set<ResourceLocation> excludeDimensions;

    public final Set<ResourceLocation> includeBiomes;
    public final Set<ResourceLocation> excludeBiomes;


    public AbstractRestriction(
            Target target,
            @Nullable Predicate<Player> condition,
            @Nullable List<ResourceLocation> includeDimensions,
            @Nullable List<ResourceLocation> excludeDimensions,
            @Nullable List<ResourceLocation> includeBiomes,
            @Nullable List<ResourceLocation> excludeBiomes,
            @Nullable Target replacement
    ) {
        this.target = target;
        this.replacement = replacement;
        this.condition = (condition != null) ? condition : DEFAULT_CONDITION;
        this.includeDimensions = Optional.ofNullable(includeDimensions).map(HashSet::new).orElseGet(HashSet::new);
        this.excludeDimensions = Optional.ofNullable(excludeDimensions).map(HashSet::new).orElseGet(HashSet::new);
        this.includeBiomes = Optional.ofNullable(includeBiomes).map(HashSet::new).orElseGet(HashSet::new);
        this.excludeBiomes = Optional.ofNullable(excludeBiomes).map(HashSet::new).orElseGet(HashSet::new);
    }

    public AbstractRestriction(
            Target target,
            @Nullable Predicate<Player> condition,
            @Nullable Target replacement
    ) {
        this(target, condition, null, null, null, null, replacement);
    }
}
