# How to create a new module
### It's actually relatively easy to create a new BetterF3 module.

### Steps
1. Open the [Help Module](../../common/src/main/java/me/cominixo/betterf3/modules/HelpModule.java) while reading
   this in order to get a better understanding of what is happening.
2. Create a class which extends `(me.cominixo.betterf3.modules.BaseModule)`
3. Instantiate the class
    1. Set `defaultNameColor` and `defaultValueColor`
    2. Set `nameColor` and `valueColor` to the same values
    3. Add lines to the `lines` list
        1. `lines.add(new me.cominixo.betterf3.utils.DebugLine({line name}));`
4. Create a `private void update(MinecraftClient)` method
    1. Place line information in here
5. In your mod startup class, include `new {your module class}.init()` to initialize your module
6. For every line you have added, add `text.betterf3.line.{line name}` with the name of the line in your language file