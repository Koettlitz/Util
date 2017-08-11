package de.dk.util.opt;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class CommandBuilder implements ArgumentBuilder {
   private final ArgumentParserBuilder parentBuilder;
   private final Command command;

   /**
    * Creates a new argument builder that belongs to the given <code>parentBuilder</code>.
    * The argument that this argument builder is building is passed to the <code>parentBuilder</code>.
    *
    * @param parentBuilder The argumentparser builder this argument builder belongs to
    * @param index The index of the argument it has in the order
    * @param name The name of the argument
    */
   protected CommandBuilder(ArgumentParserBuilder parentBuilder, int index, String name) throws NullPointerException {
      this.parentBuilder = parentBuilder;
      this.command = new Command(index, name);
   }

   /**
    * Creates a new argument builder that belongs to the given <code>parentBuilder</code>.
    * The argument that this argument builder is building is passed to the <code>parentBuilder</code>.
    *
    * @param parentBuilder The argumentparser builder this argument builder belongs to
    * @param index The index of the argument it has in the order
    * @param name The name of the argument
    */
   protected CommandBuilder(int index, String name) {
      this(null, index, name);
   }

   @Override
   public ArgumentParserBuilder build() throws UnsupportedOperationException, IllegalStateException {
      if (parentBuilder == null)
         throw new UnsupportedOperationException("This command builder didn't have a parent builder.");

      if (command.getParser() == null)
         throw new IllegalStateException("No parser specified");

      return parentBuilder.addCommand(command);
   }

   @Override
   public Command buildAndGet() {
      parentBuilder.addCommand(command);
      return command;
   }

   @Override
   public CommandBuilder setMandatory(boolean mandatory) {
      command.setMandatory(mandatory);
      return this;
   }

   @Override
   public CommandBuilder setDescription(String description) {
      command.setDescription(description);
      return this;
   }

   /**
    * Build an argumentparser with an argumentparser builder that is a child builder of this command builder.
    * The returned builders {@link ArgumentParserBuilder#build()} method will return this builder again.
    *
    * @return An <code>ArgumentParserBuilder</code> as a child builder of this builder
    */
   public ArgumentParserBuilder buildParser() {
      ArgumentParserBuilder childBuilder = new ArgumentParserBuilder(this);
      return childBuilder;
   }

   /**
    * Set the argumentparser for the command.
    *
    * @param parser The argumentparser to parse the arguments of the command
    *
    * @return This command builder to go on
    */
   public CommandBuilder setParser(ArgumentParser parser) {
      command.setParser(parser);
      return this;
   }

   @Override
   public boolean isChild() {
      return parentBuilder != null;
   }
}