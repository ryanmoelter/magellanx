package com.ryanmoelter.magellanx.core.lifecycle

public class CreateAndAttachFieldToLifecycleWhenStartedDelegate<Field>(
  public val fieldSupplier: () -> Field,
) : LifecycleAware {
  public var field: Field? = null
    protected set

  override fun start() {
    field = fieldSupplier()
  }

  override fun stop() {
    field = null
  }
}

public fun <Field> LifecycleOwner.createAndAttachFieldToLifecycleWhenStarted(
  fieldSupplier: () -> Field,
): AttachFieldToLifecycleDelegate<
  CreateAndAttachFieldToLifecycleWhenStartedDelegate<Field>,
  Field?,
> =
  attachFieldToLifecycle(
    lifecycleAware = CreateAndAttachFieldToLifecycleWhenStartedDelegate(fieldSupplier),
    getPropertyType = { it.field },
  )
