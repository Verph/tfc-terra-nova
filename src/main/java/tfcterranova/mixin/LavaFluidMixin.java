package tfcterranova.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import tfcterranova.config.TNConfig;
import tfcterranova.util.TNHelpers;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin extends FlowingFluid
{
    @Unique private BlockPos blockPos;

    @Inject(method = "spreadTo", at = @At("HEAD"))
    protected void inject$spreadTo(LevelAccessor level, BlockPos pos, BlockState state, Direction direction, FluidState fluidState, CallbackInfo ci)
    {
        blockPos = pos;
    }

    @Overwrite(remap = false)
    @Override
    public int getSlopeFindDistance(LevelReader level)
    {
        if (level instanceof ServerLevel serverLevel && blockPos != null && !level.dimensionType().ultraWarm())
        {
            switch (TNHelpers.rockType(serverLevel, blockPos).displayCategory())
            {
                case FELSIC_IGNEOUS_EXTRUSIVE:
                case FELSIC_IGNEOUS_INTRUSIVE:
                    return TNConfig.COMMON.slopeDistanceFelsic.get();
                case METAMORPHIC:
                    return TNConfig.COMMON.slopeDistanceMetamorphic.get();
                case SEDIMENTARY:
                    return TNConfig.COMMON.slopeDistanceSedimentary.get();
                case INTERMEDIATE_IGNEOUS_EXTRUSIVE:
                case INTERMEDIATE_IGNEOUS_INTRUSIVE:
                    return TNConfig.COMMON.slopeDistanceIntermediate.get();
                case MAFIC_IGNEOUS_EXTRUSIVE:
                case MAFIC_IGNEOUS_INTRUSIVE:
                    return TNConfig.COMMON.slopeDistanceMafic.get();
                default:
                    return 2;
            }
        }
        return level.dimensionType().ultraWarm() ? 4 : 2;
    }

    @Overwrite(remap = false)
    @Override
    public int getDropOff(LevelReader level)
    {
        if (level instanceof ServerLevel serverLevel && blockPos != null && !level.dimensionType().ultraWarm())
        {
            switch (TNHelpers.rockType(serverLevel, blockPos).displayCategory())
            {
                case FELSIC_IGNEOUS_EXTRUSIVE:
                case FELSIC_IGNEOUS_INTRUSIVE:
                    return TNConfig.COMMON.dropOffFelsic.get();
                case METAMORPHIC:
                    return TNConfig.COMMON.dropOffMetamorphic.get();
                case SEDIMENTARY:
                    return TNConfig.COMMON.dropOffSedimentary.get();
                case INTERMEDIATE_IGNEOUS_EXTRUSIVE:
                case INTERMEDIATE_IGNEOUS_INTRUSIVE:
                    return TNConfig.COMMON.dropOffIntermediate.get();
                case MAFIC_IGNEOUS_EXTRUSIVE:
                case MAFIC_IGNEOUS_INTRUSIVE:
                    return TNConfig.COMMON.dropOffMafic.get();
                default:
                    return 2;
            }
        }
        return level.dimensionType().ultraWarm() ? 1 : 2;
    }

    @Overwrite(remap = false)
    @Override
    public int getTickDelay(LevelReader level)
    {
        if (level instanceof ServerLevel serverLevel && blockPos != null && !level.dimensionType().ultraWarm())
        {
            switch (TNHelpers.rockType(serverLevel, blockPos).displayCategory())
            {
                case FELSIC_IGNEOUS_EXTRUSIVE:
                case FELSIC_IGNEOUS_INTRUSIVE:
                    return TNConfig.COMMON.tickDelayFelsic.get();
                case METAMORPHIC:
                    return TNConfig.COMMON.tickDelayMetamorphic.get();
                case SEDIMENTARY:
                    return TNConfig.COMMON.tickDelaySedimentary.get();
                case INTERMEDIATE_IGNEOUS_EXTRUSIVE:
                case INTERMEDIATE_IGNEOUS_INTRUSIVE:
                    return TNConfig.COMMON.tickDelayIntermediate.get();
                case MAFIC_IGNEOUS_EXTRUSIVE:
                case MAFIC_IGNEOUS_INTRUSIVE:
                    return TNConfig.COMMON.tickDelayMafic.get();
                default:
                    return 30;
            }
        }
        return level.dimensionType().ultraWarm() ? 10 : 30;
    }
}
