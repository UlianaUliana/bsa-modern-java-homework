package com.binary_studio.fleet_commander.core.ship;

import com.binary_studio.fleet_commander.core.common.PositiveInteger;
import com.binary_studio.fleet_commander.core.exceptions.InsufficientPowergridException;
import com.binary_studio.fleet_commander.core.exceptions.NotAllSubsystemsFitted;
import com.binary_studio.fleet_commander.core.ship.contract.ModularVessel;
import com.binary_studio.fleet_commander.core.subsystems.contract.AttackSubsystem;
import com.binary_studio.fleet_commander.core.subsystems.contract.DefenciveSubsystem;

public final class DockedShip implements ModularVessel {

	private String name;

	private PositiveInteger shieldHP;

	private PositiveInteger hullHP;

	private PositiveInteger powergridOutput;

	private PositiveInteger capacitorAmount;

	private PositiveInteger capacitorRechargeRate;

	private PositiveInteger speed;

	private PositiveInteger size;

	private AttackSubsystem attackSubsystem;

	private DefenciveSubsystem defenciveSubsystem;

	private DockedShip(String name, PositiveInteger shieldHP, PositiveInteger hullHP,
						PositiveInteger powergridOutput, PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate,
						PositiveInteger speed, PositiveInteger size) {
		this.name = name;
		this.shieldHP = shieldHP;
		this.hullHP = hullHP;
		this.powergridOutput = powergridOutput;
		this.capacitorAmount = capacitorAmount;
		this.capacitorRechargeRate = capacitorRechargeRate;
		this.speed = speed;
		this.size = size;
	}

	public static DockedShip construct(String name, PositiveInteger shieldHP, PositiveInteger hullHP,
			PositiveInteger powergridOutput, PositiveInteger capacitorAmount, PositiveInteger capacitorRechargeRate,
			PositiveInteger speed, PositiveInteger size) {
		return new DockedShip(name, shieldHP, hullHP, powergridOutput, capacitorAmount,
				capacitorRechargeRate, speed, size);
	}

	@Override
	public void fitAttackSubsystem(AttackSubsystem subsystem) throws InsufficientPowergridException {
		if (subsystem == null) {
			this.attackSubsystem = null;
		}
		else {
			if (this.defenciveSubsystem == null) {
				if (subsystem.getPowerGridConsumption().value() > this.powergridOutput.value()) {
					int missingPowergrid = subsystem.getPowerGridConsumption().value() - this.powergridOutput.value();
					throw new InsufficientPowergridException(missingPowergrid);
				}
			}
			else {
				if (subsystem.getPowerGridConsumption().value() + this.defenciveSubsystem.getPowerGridConsumption().value() > this.powergridOutput.value()) {
					int missingPowergrid = subsystem.getPowerGridConsumption().value() + this.defenciveSubsystem.getPowerGridConsumption().value() - this.powergridOutput.value();
					throw new InsufficientPowergridException(missingPowergrid);
				}
			}
			this.attackSubsystem = subsystem;
		}
	}

	@Override
	public void fitDefensiveSubsystem(DefenciveSubsystem subsystem) throws InsufficientPowergridException {
		if (subsystem == null) {
			this.defenciveSubsystem = null;
		}
		else {
			if (this.attackSubsystem == null) {
				if (subsystem.getPowerGridConsumption().value() > this.powergridOutput.value()) {
					int missingPowergrid = subsystem.getPowerGridConsumption().value() - this.powergridOutput.value();
					throw new InsufficientPowergridException(missingPowergrid);
				}
			}
			else {
				if (subsystem.getPowerGridConsumption().value() + this.attackSubsystem.getPowerGridConsumption().value() > this.powergridOutput.value()) {
					int missingPowergrid = subsystem.getPowerGridConsumption().value() + attackSubsystem.getPowerGridConsumption().value() - this.powergridOutput.value();
					throw new InsufficientPowergridException(missingPowergrid);
				}
			}
			this.defenciveSubsystem = subsystem;
		}
	}

	public CombatReadyShip undock() throws NotAllSubsystemsFitted {
		if (this.attackSubsystem == null) {
			if (this.defenciveSubsystem == null) {
				throw  NotAllSubsystemsFitted.bothMissing();
			}
			else {
				throw NotAllSubsystemsFitted.attackMissing();
			}
		}
		else if (this.defenciveSubsystem == null) {
			throw NotAllSubsystemsFitted.defenciveMissing();
		}
		else {
			return new CombatReadyShip(this.name, this.shieldHP, this.hullHP,
					this.powergridOutput, this.capacitorAmount, this.capacitorRechargeRate,
					this.speed, this.size, this.attackSubsystem, this.defenciveSubsystem);
		}
	}

}
