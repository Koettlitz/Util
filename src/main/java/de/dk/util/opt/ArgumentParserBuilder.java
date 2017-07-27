package de.dk.util.opt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A builder class to build an {@link ArgumentParser}.
 * To build arguments and options for the argumentparser this builder creates subbuilders for arguments and options.
 * Those subbuilders return this parentbuilder in their build mehtod
 * to get back to building new Arguments and options.
 * An example of the usage:<br><br>
 * <code>
 * ArgumentParserBuilder builder = new ArgumentParserBuilder();<br>
 * ArgumentParser parser = builder.buildArgument("foo")<br>
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp.setMandatory(false)<br>
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp.setDescription("description of foo")<br>
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp.build()<br>
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp.buildOption('b', "bar")<br>
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp.setDescription("description of option bar")<br>
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp.setExpectsValue(true)<br>
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp.build()<br>
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
   &nbsp&nbsp&nbsp.build();<br>
 * </code>
 *
 * @author David Koettlitz
 * <br>Erstellt am 24.07.2017
 */
public class ArgumentParserBuilder {
   private List<ExpectedPlainArgument> arguments = new ArrayList<>();
   private Map<Character, ExpectedOption> options = new LinkedHashMap<>();
   private Map<String, ExpectedOption> longOptions = new LinkedHashMap<>();
   private Map<String, Command> commands = new LinkedHashMap<>(0);

   private CommandBuilder parentBuilder;

   private int argCount;

   public ArgumentParserBuilder(CommandBuilder parentBuilder) {
      this.parentBuilder = parentBuilder;
   }

   public ArgumentParserBuilder() {
      this(null);
   }

   /**
    * Builds the argumentparser, that will be able to parse all the arguments and options specified.
    *
    * @return A new argumentparser
    */
   public ArgumentParser buildAndGet() {
      ArgumentParser parser = new ArgumentParser(arguments, options, longOptions, commands);
      if (parentBuilder != null)
         parentBuilder.setParser(parser);

      return parser;
   }

   public CommandBuilder build() {
      if (parentBuilder == null)
         throw new UnsupportedOperationException("This ArgumentParserBuilder didn't have a parent builder.");

      buildAndGet();
      return parentBuilder;
   }

   /**
    * Creates a new argument builder to build an argument.
    * The build method of that argument builder will return this argumentparser builder again.
    *
    * @param name The name of the argument
    *
    * @return An argument builder that will be a child builder of this builder
    *
    * @throws NullPointerException If the given <code>name</code> is <code>null</code>
    */
   public PlainArgumentBuilder buildArgument(String name) throws NullPointerException {
      return new PlainArgumentBuilder(this, argCount++, name);
   }

   /**
    * Adds an argument to this builder, that the resulting argument parser will be able to parse.
    * The argument will be a simple mandatory argument.
    * To create define specific arguments use the {@link ArgumentParserBuilder#buildArgument(String)} method.
    *
    * @param name The name of the argument
    *
    * @return This argumentparser builder to go on
    *
    * @throws NullPointerException If the given <code>name</code> is <code>null</code>
    */
   public ArgumentParserBuilder addArgument(String name) throws NullPointerException {
      return addArgument(new ExpectedPlainArgument(argCount++, name));
   }

   /**
    * Adds an argument to this builder, that the resulting argument parser will be able to parse.
    * The argument will be a simple mandatory argument.
    * To create define specific arguments use the {@link ArgumentParserBuilder#buildArgument(String)} method.
    *
    * @param name The name of the argument
    * @param description The description of the argument
    *
    * @return This argumentparser builder to go on
    *
    * @throws NullPointerException If the given <code>name</code> is <code>null</code>
    */
   public ArgumentParserBuilder addArgument(String name, String description) throws NullPointerException {
      return addArgument(new ExpectedPlainArgument(argCount++, name, description));
   }

   /**
    * Adds an argument to this builder, that the resulting argument parser will be able to parse.
    *
    * @param name The name of the argument
    * @param mandatory If the argument is mandatory or not
    * @param description The description of the argument
    *
    * @return This argumentparser builder to go on
    *
    * @throws NullPointerException If the given <code>name</code> is <code>null</code>
    */
   public ArgumentParserBuilder addArgument(String name, boolean mandatory, String description) throws NullPointerException {
      return addArgument(new ExpectedPlainArgument(argCount++, name, mandatory, description));
   }

   /**
    * Adds an argument to this builder, that the resulting argument parser will be able to parse.
    *
    * @param argument The argument to be added
    *
    * @return This argumentparser builder to go on
    */
   protected ArgumentParserBuilder addArgument(ExpectedPlainArgument argument) {
      arguments.add(argument);
      return this;
   }

   /**
    * Creates a new option builder to build an option.
    * The build method of that option builder will return this argumentparser builder again.
    *
    * @param key The key of the option
    * @param name The name of the option
    *
    * @return An option builder that will be a child builder of this builder
    *
    * @throws NullPointerException If the given <code>name</code> is <code>null</code>
    */
   public OptionBuilder buildOption(char key, String name) throws NullPointerException {

      return new OptionBuilder(this, argCount++, key, name);
   }

   /**
    * Creates a new option builder to build an option.
    * The build method of that option builder will return this argumentparser builder again.
    *
    * @param longKey The long key of the option
    * @param name The name of the option
    *
    * @return An option builder that will be a child builder of this builder
    *
    * @throws NullPointerException If the given <code>name</code> is <code>null</code>
    */
   public OptionBuilder buildOption(String longKey, String name) throws NullPointerException {

      return new OptionBuilder(this, argCount++, longKey, name);
   }

   /**
    * Adds an option to this builder, that the resulting argument parser will be able to parse.
    * The option will be a simple flag without any value.
    * To add a more specific option use the {@link ArgumentParserBuilder#buildOption(char, String)} method.
    *
    * @param key The key of the option
    * @param name The name of the option
    *
    * @return This argumentparser builder to go on
    *
    * @throws NullPointerException If the given <code>name</code> is <code>null</code>
    */

   public ArgumentParserBuilder addOption(char key, String name) throws NullPointerException {
      return addOption(new ExpectedOption(argCount++, key, name));
   }

   /**
    * Adds an option to this builder, that the resulting argument parser will be able to parse.
    * The option will be a simple flag without any value.
    * To add a more specific option use the {@link ArgumentParserBuilder#buildOption(char, String)} method.
    *
    * @param longKey The longKey of the option
    * @param name The name of the option
    *
    * @return This argumentparser builder to go on
    *
    * @throws NullPointerException If the given <code>name</code> is <code>null</code>
    */
   public ArgumentParserBuilder addOption(String longKey, String name) throws NullPointerException {
      return addOption(new ExpectedOption(argCount++, longKey, name));
   }

   /**
    * Adds an option to this builder, that the resulting argument parser will be able to parse.
    * The option will be a simple flag without any value.
    * To add a more specific option use the {@link ArgumentParserBuilder#buildOption(char, String)} method.
    *
    * @param key The key of the option
    * @param name The name of the option
    * @param description The description of the option
    *
    * @return This argumentparser builder to go on
    *
    * @throws NullPointerException If the given <code>name</code> is <code>null</code>
    */
   public ArgumentParserBuilder addOption(char key, String name, String description) throws NullPointerException {
      return addOption(new ExpectedOption(argCount++, key, name, description));
   }

   /**
    * Adds an option to this builder, that the resulting argument parser will be able to parse.
    *
    * @param option The option to be added
    *
    * @return This argumentparser builder to go on
    */
   protected ArgumentParserBuilder addOption(ExpectedOption option) {
      options.put(option.getKey(), option);
      if (option.getLongKey() != null)
         longOptions.put(option.getLongKey(), option);

      return this;
   }

   /**
    * Creates a new command builder to build a command.
    * The build method of that command builder will return this argumentparser builder again.
    *
    * @param name The name of the command
    *
    * @return A command builder that will be a child builder of this builder
    *
    * @throws NullPointerException If the given <code>name</code> is <code>null</code>
    */
   public CommandBuilder buildCommand(String name) throws NullPointerException {
      return new CommandBuilder(this, argCount++, name);
   }

   protected ArgumentParserBuilder addCommand(Command command) {
      commands.put(command.getName(), command);
      return this;
   }
}