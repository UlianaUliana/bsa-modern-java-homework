# Ласкаво просимо у FleetCommander.
У цьому завданні вашим завданням буде створити покроковий симулятор космічних битв. Ми вже створили частина інфраструктури і інтерфейси, щоб вам допомогти.

# Rules of engagement
Основа даної гри - космічні кораблі.
Кораблі мають кілька основних базових характеристик:
 - Швидкість `Velocity` вимірюється в` m / s`,
 - Розмір `Size` вимірюється в` m`,
 - Потужність реактора `Power Grid (PG)` вимірюється в `MW`
 - Заряд аккамулятора `Capacitor (Cap)` вимірюється в `GA / H`
 - Відновлення заряду аккамулятора в хід `Cap regen`
 - Стан корпусу і щита `Hull / Shield` ізмеряеться в` EHP`

Кожен корабель складається з двох підсистем: захисної і атакуючої.
Спочатку корабель знаходиться в доці, в цьому режимі до корабля потрібно підключати модулі. Корабель не може вийти з доку, поки на нього не встановлена ​​захисна і атакуюча підсистеми.
Кожна система вимагає певний кількість `PG`. Споживання `PG` підсумовується, тобто для того, щоб встановити дві підсистеми з вимогою до `PG` 50 MW і 25 MW потрібно корабель з 25 + 50 => 75 MW. При нестачі потужності реактора необхідно викинути `NotEnoughPowergridException`.
Систему можна зняти з корабля, передавши в відповідні методи `null`
Атакуюча підсистема працює наступним чином: в її метод attack передається ціль, після чого проводиться розрахунок завданого урону за такою формулою:
```
sizeReductionModifier = when targetSize> = optimalSize -> 1
else targetSize / optimalSize
speedReductionModifier = when targetSpeed ​​<= optimalSpeed ​​-> 1
                         else optimalSpeed ​​/ (2 * targetSpeed)
damage = baseDamage * min (sizeReductionModifier, speedReductionModifier)
```

Отриманий урон округляється до цілого числа з округленням у більшу сторону (тобто при уроні більше 0, атака не може нанести менше 1 одиниці урону). Для атаки потрібна певна кількість заряду аккамулятора. При спробі атакувати з недостатньою кількістю заряду аккамулятора необхідно повернути порожній Optional.

Захисна підсистема трохи складніше: вона має пасивні і активні ефекти. Для активації захисної системи необхідно певна кількість заряду. Активація відновлює `EHP` корпусу і щита. При спробі активувати захисну систему з недостатньою кількістю заряду необхідно повернути порожній Optional. Також захистна система пасивно знижує вхідний урон на певний відсоток. При зменшенні урону відбувається округлення до найближчого цілого в більшу сторону. Урон не може бути знижений більш ніж на 95%.

Корабель оповіщається про початок і кінець ходу. В кінці ходу корабель відновлює заряд аккамулятора. Заряд аккамулятора не може бути заряджений більше початкового значення.

При отриманні шкоди у корабля зменшується кількість щита і корпусу. Спочатку шкоди отримує щит. Якщо здоров'я щита впало до 0, то залишок шкоди поулчает корпус. При регенерації щит і корпус відновлюються, використовуючи регенерацію захисної підсистеми. Здоров'я щитів і корпусу не може перевищувати початкове значення.

# Tips and tricks
1. Ваше завдання - імплементувати такі класи:
  `com.binary_studio.fleet_commander.core.ship.DockedVessel`
  `com.binary_studio.fleet_commander.core.ship.CombatReadyVessel`
  `com.binary_studio.fleet_commander.core.subsystems.DefenciveSubsystemImpl`
  `com.binary_studio.fleet_commander.core.subsystems.AttackSubsystemImpl`
2. Подивіться тести, це допоможе зрозуміти очікуванну поведінку
3. Не можна змінювати контракт існуючих класів (сигнатури методів), але змінювати їх реалізацію можна і потрібно. Так само ви можете створювати нові методи в уже існуючих класах і інтерфейсах або створювати нові класи та інтерфейси.
4. Почніть виконання завдання з імплементації підсистем, а потім перейдіть до імплеменатції корабля.
5. В даному завданні можете не хвилюватися про null-safety, ми не будемо передавати null в параметрах, окрім тих місць, де це описано умовами завдання.

Успіхів!