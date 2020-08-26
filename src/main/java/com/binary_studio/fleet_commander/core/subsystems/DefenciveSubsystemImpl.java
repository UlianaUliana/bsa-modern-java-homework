package com.binary_studio.fleet_commander.core.subsystems;

import com.binary_studio.fleet_commander.core.actions.attack.AttackAction;
import com.binary_studio.fleet_commander.core.actions.defence.RegenerateAction;
import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.subsystems.contract.DefenciveSubsystem;

public final class DefenciveSubsystemImpl implements DefenciveSubsystem {

	private String name;

	private PositiveInteger powergridConsumption;

	private PositiveInteger capacitorConsumption;

	private PositiveInteger impactReductionPercent;

	private PositiveInteger shieldRegeneration;

	private PositiveInteger hullRegeneration;

	private final PositiveInteger maxShieldRegeneration;

	private final PositiveInteger maxHullRegeneration;

	private DefenciveSubsystemImpl(String name, PositiveInteger powergridConsumption, PositiveInteger capacitorConsumption, PositiveInteger impactReductionPercent, PositiveInteger shieldRegeneration, PositiveInteger hullRegeneration) {
		this.name = name;
		this.powergridConsumption = powergridConsumption;
		this.capacitorConsumption = capacitorConsumption;
		this.impactReductionPercent = impactReductionPercent;
		this.shieldRegeneration = shieldRegeneration;
		this.hullRegeneration = hullRegeneration;
		this.maxShieldRegeneration = shieldRegeneration;
		this.maxHullRegeneration = hullRegeneration;
	}

	public static DefenciveSubsystemImpl construct(String name, PositiveInteger powergridConsumption,
			PositiveInteger capacitorConsumption, PositiveInteger impactReductionPercent,
			PositiveInteger shieldRegeneration, PositiveInteger hullRegeneration) throws IllegalArgumentException {
		// TODO:
		if (name.equals(null) || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Name should be not null and not empty");
		}
		return new DefenciveSubsystemImpl(name, powergridConsumption, capacitorConsumption, impactReductionPercent, shieldRegeneration, hullRegeneration);
	}

	@Override
	public PositiveInteger getPowerGridConsumption() {
		return this.powergridConsumption;
	}

	@Override
	public PositiveInteger getCapacitorConsumption() {
		return this.capacitorConsumption;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public PositiveInteger getMaxShieldRegeneration() {
		return this.maxShieldRegeneration;
	}

	@Override
	public PositiveInteger getMaxHullRegeneration() {
		return this.maxHullRegeneration;
	}

	@Override
	public void setShieldRegeneration(PositiveInteger shieldRegeneration) {
		this.shieldRegeneration = shieldRegeneration;
	}

	@Override
	public void setHullRegeneration(PositiveInteger hullRegeneration) {
		this.hullRegeneration = hullRegeneration;
	}

	@Override
	public AttackAction reduceDamage(AttackAction incomingDamage) {
		int reducedDamage = (int) Math.ceil(this.impactReductionPercent.value() <= 95 ?
				(1 - this.impactReductionPercent.value() * 0.01) * incomingDamage.damage.value() :
				0.05 * incomingDamage.damage.value());
		return new AttackAction(PositiveInteger.of(reducedDamage), incomingDamage.attacker, incomingDamage.target, incomingDamage.weapon);
	}

	@Override
	public RegenerateAction regenerate() {
		return new RegenerateAction(this.shieldRegeneration, this.hullRegeneration);
	}

}
