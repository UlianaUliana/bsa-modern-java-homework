package com.binary_studio.fleet_commander.core.ship;

import java.util.Optional;

import com.binary_studio.fleet_commander.core.actions.attack.AttackAction;
import com.binary_studio.fleet_commander.core.actions.defence.AttackResult;
import com.binary_studio.fleet_commander.core.actions.defence.RegenerateAction;
import com.binary_studio.fleet_commander.core.common.Attackable;
import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.ship.contract.CombatReadyVessel;
import com.binary_studio.fleet_commander.core.subsystems.contract.AttackSubsystem;
import com.binary_studio.fleet_commander.core.subsystems.contract.DefenciveSubsystem;

public final class CombatReadyShip implements CombatReadyVessel {

	private String name;

	private PositiveInteger shieldHP;

	private PositiveInteger hullHP;

	private PositiveInteger powergridOutput; ////not used

	private PositiveInteger capacitorAmount;

	private PositiveInteger capacitorRechargeRate;

	private PositiveInteger currentSpeed;

	private PositiveInteger size;

	private AttackSubsystem attackSubsystem;

	private DefenciveSubsystem defenciveSubsystem;

	private final PositiveInteger initialShieldHP;

	private final PositiveInteger initialHullHP;

	private final PositiveInteger initialCapacitorAmount;

	CombatReadyShip(String name, PositiveInteger shieldHP, PositiveInteger hullHP,
					PositiveInteger powergridOutput, PositiveInteger capacitorAmount,
					PositiveInteger capacitorRechargeRate, PositiveInteger currentSpeed,
					PositiveInteger size, AttackSubsystem attackSubsystem, DefenciveSubsystem defenciveSubsystem) {
		this.name = name;
		this.shieldHP = shieldHP;
		this.hullHP = hullHP;
		this.powergridOutput = powergridOutput;
		this.capacitorAmount = capacitorAmount;
		this.capacitorRechargeRate = capacitorRechargeRate;
		this.currentSpeed = currentSpeed;
		this.size = size;
		this.attackSubsystem = attackSubsystem;
		this.defenciveSubsystem = defenciveSubsystem;
		this.initialShieldHP = shieldHP;
		this.initialHullHP = hullHP;
		this.initialCapacitorAmount = capacitorAmount;
	}
	@Override
	public void endTurn() {
		// TODO: can't be bigger then initial value!
		PositiveInteger finalCapacitorAmount = PositiveInteger.of(this.capacitorRechargeRate.value() + this.capacitorAmount.value());
		this.capacitorAmount = PositiveInteger.of(finalCapacitorAmount.value() > this.initialCapacitorAmount.value() ?
				this.initialCapacitorAmount.value() - this.capacitorAmount.value() :
				finalCapacitorAmount.value());
	}

	@Override
	public void startTurn() {
		// TODO: nothing

	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public PositiveInteger getSize() {
		return this.size;
	}

	@Override
	public PositiveInteger getCurrentSpeed() {
		return this.currentSpeed;
	}


	@Override
	public Optional<AttackAction> attack(Attackable target) {
		// TODO:
		if (this.capacitorAmount.value() < this.attackSubsystem.getCapacitorConsumption().value()) {
			return Optional.empty();
		}
		else {
			this.capacitorAmount = PositiveInteger.of(this.capacitorAmount.value() - this.attackSubsystem.getCapacitorConsumption().value());
			PositiveInteger damage = this.attackSubsystem.attack(target);
			AttackAction attackAction = new AttackAction(damage, this, target, this.attackSubsystem);
			return Optional.of(attackAction);
		}

	}

	@Override
	public AttackResult applyAttack(AttackAction attack) {
		AttackAction reducedAttack = this.defenciveSubsystem.reduceDamage(attack);
		if (reducedAttack.damage.value() < this.shieldHP.value()) {
			this.shieldHP = PositiveInteger.of(this.shieldHP.value() - reducedAttack.damage.value());
			return  new AttackResult.DamageRecived(attack.weapon, reducedAttack.damage, this);
		}
		else {
			PositiveInteger restDamage = PositiveInteger.of(reducedAttack.damage.value() - shieldHP.value());
			this.shieldHP = PositiveInteger.of(0);
			if (restDamage.value() < hullHP.value()) {
				this.hullHP = this.shieldHP = PositiveInteger.of(hullHP.value() - restDamage.value());
				return new AttackResult.DamageRecived(attack.weapon, reducedAttack.damage, this);
			}
			else {
				return new AttackResult.Destroyed();
			}
		}
	}

	@Override
	public Optional<RegenerateAction> regenerate() {
		// TODO:
		if (this.capacitorAmount.value() < this.defenciveSubsystem.getCapacitorConsumption().value()) {
			return Optional.empty();
		}
		else {
			PositiveInteger finalShieldRegeneration = PositiveInteger.of(this.shieldHP.value() + this.defenciveSubsystem.getMaxShieldRegeneration().value());
			PositiveInteger finalHullRegeneration = PositiveInteger.of(this.hullHP.value() + this.defenciveSubsystem.getMaxHullRegeneration().value());

			if (finalShieldRegeneration.value() > this.initialShieldHP.value()) {
				PositiveInteger shieldRegenerationDiff = PositiveInteger.of(this.initialShieldHP.value() - this.shieldHP.value());
				this.defenciveSubsystem.setShieldRegeneration(shieldRegenerationDiff);
			}
			else {
				this.defenciveSubsystem.setShieldRegeneration(this.defenciveSubsystem.getMaxShieldRegeneration());
			}
			if (finalHullRegeneration.value() > initialHullHP.value()) {
				PositiveInteger hullRegenerationDiff = PositiveInteger.of(initialHullHP.value() - hullHP.value());
				this.defenciveSubsystem.setHullRegeneration(hullRegenerationDiff);
			}
			else {
				this.defenciveSubsystem.setHullRegeneration(this.defenciveSubsystem.getMaxHullRegeneration());
			}
			RegenerateAction regenerateAction = this.defenciveSubsystem.regenerate();
			this.shieldHP = PositiveInteger.of(this.shieldHP.value() + regenerateAction.shieldHPRegenerated.value());
			this.hullHP = PositiveInteger.of(this.hullHP.value() + regenerateAction.hullHPRegenerated.value());
			this.capacitorAmount = PositiveInteger.of(this.capacitorAmount.value() - this.defenciveSubsystem.getCapacitorConsumption().value());
			return Optional.of(regenerateAction);
		}

	}

}
